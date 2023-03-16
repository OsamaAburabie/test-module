import { useEffect, useState } from "react";
import * as TestModule from "test-module";
import useIsPlaying from "./useIsPlaying";
import usePlayerState from "./usePlayerState";

const useProgress = (updatingInterval: number = 1) => {
  const { isPlaying } = useIsPlaying();
  const { state } = usePlayerState();
  const [progress, setProgress] = useState({
    position: 0,
    duration: 0,
  });

  useEffect(() => {
    if (isPlaying) {
      const interval = setInterval(() => {
        setProgress({
          position: TestModule.getPosition(),
          duration: TestModule.getDuration(),
        });
      }, 100);
      return () => clearInterval(interval);
    }
  }, [isPlaying]);

  useEffect(() => {
    if (state === TestModule.State.STATE_IDLE) {
      setProgress({
        position: 0,
        duration: 0,
      });
    }
  }, [state]);

  return { progress };
};

export default useProgress;
