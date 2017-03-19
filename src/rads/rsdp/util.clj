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

(defn- consume-events [pid handle events trigger]
  (let [stop (async/chan)]
    (go-loop []
      (let [[event ch] (async/alts! [stop events] :priority true)]
        (when-not (= ch stop)
          (<! (handle pid event trigger))
          (recur))))
    stop))

(defrecord AsyncProcess [pid handle events trigger stop]
  component/Lifecycle
  (start [p]
    (let [stop (consume-events p handle events trigger)]
      (async/put! trigger [pid :init])
      (assoc started :stop stop)))
  (stop [p]
    (async/close! (:stop p))
    (assoc p :stop nil)))

(defn new-async-process [handle events trigger]
  (map->AsyncProcess
    {:handle handle
     :events events
     :trigger trigger
     :pid (UUID/randomUUID)}))
