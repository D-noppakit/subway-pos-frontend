import { create } from 'zustand';
import { persist } from 'zustand/middleware';
const useStore = create(
    persist(
        (set) => ({
            name: "dom",
            count: 0,
            increaseCount: () => set((state) => ({ count: state.count + 1 })),
            resetCount: () => set({ count: 0 }),
            decreaseCount: () => set((state) => ({ count: state.count - 1 }))
        }),
        {
            name: 'Store', // ชื่อของ item ใน localStorage
            getStorage: () => localStorage, // กำหนดให้ใช้ localStorage
        }
    )
);
export default useStore;