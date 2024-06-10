"use client"
import useSWR from 'swr'
async function fetcherData(url, data) {
    let res = await fetch(url);
    let data = await res.json()
    data.time = new Date().getTime()
    return data
}
export default function useClient() {
    const { data, error, isLoading } = useSWR(['https://api.coindesk.com/v1/bpi/currentprice.json', { key: 'value' }], fetcherData);
    return (
        <>
            <div>useClient</div>
            <div>{JSON.stringify(data)}</div>
        </>
    )
}
