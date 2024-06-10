
import Image from "next/image";


export default function Counter() {
  return (
    <main className="flex h-screen w-screen">
      <section className=" h-full w-1/3 relative">
        <div className="w-[70%] absolute  top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 ">
          <Image src={"/imgs/subway-logo-login.png"} width={20000} height={1000} />
        </div>
        <div className="w-full h-full">
          <Image src={"/imgs/login-bg-subway.png"} width={500} height={1000} style={{ width: "100%", height: "100%" }} />
        </div>
      </section>
      <section className="bg-[#03741E] h-full w-2/3 relative">
        <div className="w-[50%] absolute  top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white h-[530px] p-[40px] gap-[56px] rounded-[32px] ">
          <div>
            {/* <Image src={"/imgs/"} width={500} height={1000} style={{ width: "100%", height: "100%" }} /> */}
          </div>
          <div>login</div>

        </div>
      </section>
    </main>
  );
}
