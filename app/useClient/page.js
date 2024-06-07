"use client"
async function fetcherData() {
    let res = await fetch("https://api.coindesk.com/v1/bpi/currentprice.json");
    let data = await res.json()
    return data
}
export default async function useClient() {
    let data = await fetcherData()
    console.log(data)
    return (
        <>
            <div>useClient</div>
            <div>{JSON.stringify(data)}</div>
        </>
    )
}
