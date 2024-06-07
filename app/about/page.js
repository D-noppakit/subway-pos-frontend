import Link from "next/link"
import "@/app/style/globals.css"

export default function Page() {
  return (
    <>
      <h1>About page!</h1>
      <div className="mt-3">
        <Link href={"/testComponent"} >
          tage Link to testComponent pages
        </Link>
      </div>
      <div className="mt-3">
        <a href={"/testComponent"} >
          tage A to testComponent pages
        </a>
      </div>
    </>
  )
}