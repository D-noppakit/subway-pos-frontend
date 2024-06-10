"use server"
import dynamic from 'next/dynamic';
const Counter = dynamic(() => import('@/components/Counter'), { ssr: false });
async function fetcherData() {
    let res = await fetch("https://api.coindesk.com/v1/bpi/currentprice.json");
    let data = await res.json()
    data.time = new Date().getTime()
    return data
}

export default async function useServer() {
    return (
        <>
            <div>useServer</div>
            <div>{JSON.stringify(data)}</div>
            <Counter />
        </>
    )
}
