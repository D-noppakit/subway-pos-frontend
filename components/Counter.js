import React from 'react';
import useStore from '@/lib/store';

const Counter = () => {
    const { count, increaseCount, resetCount, decreaseCount } = useStore();

    return (
        <div>
            <h1>Count: {count}</h1>
            <button onClick={increaseCount}>Increase</button>
            <button onClick={decreaseCount}>decreaseCount</button>
            <button onClick={resetCount}>Reset</button>
        </div>
    );
};

export default Counter;