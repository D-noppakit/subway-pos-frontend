import Link from "next/link"
import Image from "next/image"
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
      <Image
        src="/imgs/messageImage_1715250896427.jpg"
        width={100}
        height={300}
        alt="Picture of the author"
      />
    </>
  )
}