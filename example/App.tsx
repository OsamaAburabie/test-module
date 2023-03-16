import { useEffect } from "react";
import { Button, StyleSheet, Text, View } from "react-native";
import * as TestModule from "test-module";
import useProgress from "./hooks/useProgress";

export default function App() {
  useEffect(() => {
    TestModule.init();
  }, []);

  const { progress } = useProgress();

  return (
    <View style={styles.container}>
      <Button
        title="Load"
        onPress={async () => {
          try {
            TestModule.loadSound(
              "https://cdn.pixabay.com/audio/2022/02/11/audio_25f3f87b24.mp3"
            );
          } catch (e) {
            console.log(e);
          }
        }}
      />
      <Button title="pause" onPress={() => TestModule.pauseSound()} />
      <Button title="start" onPress={() => TestModule.playSound()} />
      <Button title="stop" onPress={() => TestModule.stop()} />
      <Button
        title="getDuration"
        onPress={() => {
          console.log(TestModule.getDuration());
        }}
      />

      <Button
        title="getPosition"
        onPress={() => {
          console.log(TestModule.getPosition());
        }}
      />

      <Text>{progress.duration}</Text>
      <Text>{progress.position}</Text>
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
