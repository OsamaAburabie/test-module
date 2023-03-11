import { useEffect, useState } from "react";
import { Button, StyleSheet, Text, View } from "react-native";
import * as TestModule from "test-module";

const usePosition = (updateRate: number) => {
  const [position, setPosition] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setPosition(TestModule.getCurrentPosition());
    }, updateRate * 1000);

    return () => clearInterval(interval);
  }, []);

  return position;
};

const useDuration = () => {
  const [duration, setDuration] = useState(0);

  useEffect(() => {
    setDuration(TestModule.getDuration());
  }, []);

  return duration;
};

const Data = () => {
  const position = usePosition(1);
  const duration = useDuration();
  return (
    <View>
      <Text>Position: {position}</Text>
      <Text>Duration: {duration}</Text>
    </View>
  );
};

export default function App() {
  return (
    <View style={styles.container}>
      <Data />
      <Text>{TestModule.hello()}</Text>

      <Button
        title="Load"
        onPress={async () => {
          try {
            const res = await TestModule.loadSound(
              "https://cdn.pixabay.com/audio/2022/02/11/audio_25f3f87b24.mp3"
            );

            if (res) {
              console.log(res);
            }
          } catch (e) {
            console.log(e);
          }
        }}
      />
      <Button title="pause" onPress={() => TestModule.pauseSound()} />
      <Button title="start" onPress={() => TestModule.playSound()} />
      <Button title="reset" onPress={() => TestModule.reset()} />
      <Button title="reset" onPress={() => TestModule.reset()} />
      <Button
        title="getDuration"
        onPress={() => {
          const duration = TestModule.getDuration();
          console.log(duration);
        }}
      />
      <Button
        title="getPosition"
        onPress={() => {
          const position = TestModule.getCurrentPosition();
          console.log(position);
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
