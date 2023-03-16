import { useEffect, useState } from "react";
import * as TestModule from "test-module";

const useIsPlaying = () => {
  const [isPlaying, setIsPlaying] = useState(false);
  useEffect(() => {
    TestModule.onIsPlayingChange((state) => {
      setIsPlaying(state.isPlaying);
    });
  }, []);

  return { isPlaying };
};

export default useIsPlaying;
