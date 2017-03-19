(ns rads.rsdp.util
  (:require
    [clojure.core.async :as async :refer [go go-loop <! >!]]
    [com.stuartsierra.component :as component])
  (:import
    (java.util UUID)))

(defonce timeouts-chan (async/chan 100))
(defonce timeouts-mult (async/mult timeouts-chan))

(defn start-timer [delta]
  (go
    (<! (async/timeout delta))
    (>! timeouts-chan [:timeout])))

(defn- consume-events [{:keys [handler events-chan trigger-chan] :as p}]
  (let [stop-chan (async/chan)]
    (go-loop []
      (let [[event ch] (async/alts! [stop-chan events-chan] :priority true)]
        (when-not (= ch stop-chan)
          (handler p event #(async/put! trigger-chan %))
          (recur))))
    stop-chan))

(defrecord AsyncProcess [pid handler events-chan trigger-chan stop-chan]
  component/Lifecycle
  (start [p]
    (let [stop-chan (consume-events p)]
      (async/put! trigger-chan [pid :init])
      (assoc p :stop-chan stop-chan)))
  (stop [p]
    (async/close! (:stop-chan p))
    (assoc p :stop-chan nil)))

(defn new-async-process [opts]
  (let [keys [:pid :handler :events-chan :trigger-chan]]
    (map->AsyncProcess (select-keys opts keys))))
