import { onMounted, onUnmounted } from 'vue'

export const useEventListener = (target, event, callback) => {
    onMounted(() => {target.addEventListener(event, callback); callback()})
    onUnmounted(() => target.removeEventListener(event, callback))
}