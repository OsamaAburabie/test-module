import { useEffect, useState } from "react";
import * as TestModule from "test-module";

const usePlayerState = () => {
  const [state, setState] = useState(TestModule.State.STATE_IDLE);
  useEffect(() => {
    TestModule.onStateChange((state) => {
      setState(state.state);
    });
  }, []);

  return { state };
};

export default usePlayerState;
