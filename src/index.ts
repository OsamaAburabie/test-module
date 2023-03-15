import {
  requireNativeModule,
  EventEmitter,
  Subscription,
} from "expo-modules-core";

import TestModule from "./TestModule";

export function hello(): string {
  return TestModule.hello();
}
export function loadSound(uri: string): Promise<string> {
  return TestModule.loadSound(uri);
}
export function pauseSound(): void {
  return TestModule.pauseSound();
}
export function playSound(): void {
  return TestModule.playSound();
}
export function reset(): void {
  return TestModule.reset();
}
export function getDuration(): number {
  return TestModule.getDuration();
}

export function getCurrentPosition(): number {
  return TestModule.getCurrentPosition();
}

const AudioModule = requireNativeModule("TestModule");
const emitter = new EventEmitter(AudioModule);

export function addOnPreparedListener(listener: (event) => void): Subscription {
  return emitter.addListener("onPrepared", listener);
}

export function onPositionChanged(
  listener: (event: { position: number }) => void
): Subscription {
  return emitter.addListener("onPosition", listener);
}

export function onCustomEvent(
  listener: (event: { custom: string }) => void
): Subscription {
  return emitter.addListener("onCustomEvent", listener);
}

export function onDurationChanged(
  listener: (event: { duration: number }) => void
): Subscription {
  return emitter.addListener("onDuration", listener);
}
