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
export function addonDurationListener(listener: (event) => void): Subscription {
  return emitter.addListener("onDuration", listener);
}
