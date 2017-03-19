(ns rads.rsdp.systems.perfect-links
  (:require
    [clojure.core.async :as async]
    [rads.rsdp.util :as util]
    [com.stuartsierra.component :as component]
    [rads.rsdp.fair-loss-links :refer [new-fair-loss-link]]
    [rads.rsdp.stubborn-links :refer [new-stubborn-link]]
    [rads.rsdp.perfect-links :refer [new-perfect-link]]))

(defn new-channels []
  (let [trigger-chan (async/chan 100)
        events-chan (async/chan 100)
        events-mult (async/mult events-chan)
        fll-events (async/chan 100)
        sl-events (async/chan 100)
        pl-events (async/chan 100)]
    (async/pipe trigger-chan events-chan)
    (async/tap util/timeouts-mult events-chan)
    (async/tap events-mult fll-events)
    (async/tap events-mult sl-events)
    (async/tap events-mult pl-events)
    {:trigger-chan trigger-chan 
     :events-chan events-chan
     :events-mult events-mult
     :fll-events fll-events
     :sl-events sl-events
     :pl-events pl-events}))

(defn new-system [{:keys [trigger-chan fll-events sl-events pl-events]
                   :as channels}]
  (-> (component/system-map
        :fll (new-fair-loss-link
               {:pid "fll"
                :events-chan fll-events
                :trigger-chan trigger-chan})
        :sl (new-stubborn-link
              {:pid "sl"
               :events-chan sl-events
               :trigger-chan trigger-chan})
        :pl (new-perfect-link
              {:pid "pl"
               :events-chan pl-events
               :trigger-chan trigger-chan}))
      (component/system-using
        {:sl [:fll]
         :pl [:sl]})))

(comment
  (require '[rads.rsdp.util :as util] :reload)
  (require '[rads.rsdp.systems.perfect-links :as rsdp] :reload)
  (require '[clojure.core.async :as async])
  (require '[com.stuartsierra.component :as component])
  (def channels (rsdp/new-channels))
  (def s (async/chan (async/dropping-buffer 100)))
  (async/tap (:events-mult channels) s)
  (async/go-loop [] (when-let [m (async/<! s)] (println m) (recur)))
  (def sys (component/start (rsdp/new-system channels))))
