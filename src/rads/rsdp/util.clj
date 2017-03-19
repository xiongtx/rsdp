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

(defn- consume-events [pid handler events trigger]
  (let [stop (async/chan)]
    (go-loop []
      (let [[event ch] (async/alts! [stop events] :priority true)]
        (when-not (= ch stop)
          (handler pid event trigger)
          (recur))))
    stop))

(defrecord AsyncProcess [pid handler events trigger stop]
  component/Lifecycle
  (start [p]
    (let [stop (consume-events p handler events trigger)]
      (async/put! trigger [pid :init])
      (assoc p :stop stop)))
  (stop [p]
    (async/close! (:stop p))
    (assoc p :stop nil)))

(defn new-async-process [opts]
  (map->AsyncProcess (select-keys opts [:handler :events :trigger :pid])))
