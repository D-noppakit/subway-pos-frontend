import React, { lazy } from 'react';
import TestComponent from '@/components/TestComponent';
import "@/app/style/globals.css";
import { Suspense } from 'react';
import Loading from '@/components/Loading';

const TestComponentSlow = lazy(() =>
    new Promise(resolve => {
        setTimeout(() => resolve({ default: TestComponent }), 3000);
    })
);

const TestComponentSlowToo = lazy(() =>
    new Promise(resolve => {
        setTimeout(() => resolve({ default: TestComponent }), 4000);
    })
);

export default function Page() {
    return (
        <>
     
            <div>page test</div>
            {false ? <div>hello</div> : <div>loading</div>   }
            <TestComponent />
            <Suspense fallback={<Loading />}>
                <TestComponentSlow />
            </Suspense>
            <Suspense fallback={<Loading />}>
                <TestComponentSlowToo />
            </Suspense>
        </>
    );
}
