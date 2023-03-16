import {
  requireNativeModule,
  EventEmitter,
  Subscription,
} from "expo-modules-core";

import TestModule from "./TestModule";

const AudioModule = requireNativeModule("TestModule");
const emitter = new EventEmitter(AudioModule);

export enum State {
  STATE_IDLE = 1,
  STATE_BUFFERING = 2,
  STATE_READY = 3,
  STATE_ENDED = 4,
}

export function init(): void {
  return TestModule.init();
}

export function loadSound(uri: string): void {
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
export function stop(): void {
  return TestModule.stop();
}
export function getDuration(): number {
  return TestModule.getDuration();
}
export function getPosition(): number {
  return TestModule.getPosition();
}

//Events
export function onStateChange(
  listener: (event: { state: State }) => void
): Subscription {
  return emitter.addListener("onStateChange", listener);
}
export function onIsPlayingChange(
  listener: (event: { isPlaying: boolean }) => void
): Subscription {
  return emitter.addListener("onIsPlayingChange", listener);
}

// Hooks
