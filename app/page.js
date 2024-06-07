
import Image from "next/image";


export default function Counter() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div>hello next</div>
      <Image
        src="/imgs/messageImage_1715250896427.jpg"
        width={100}
        height={300}
        alt="Picture of the author"
      />
    </main>
  );
}
