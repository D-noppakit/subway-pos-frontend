export default async function RootLayout({ children }) {
    return (
            <section>
                <div className="w-screen bg-red-400 text-white">header</div>
                <main>{children}</main>
                <div  className="w-screen bg-red-400 text-white">footer</div>
            </section>
    )
}