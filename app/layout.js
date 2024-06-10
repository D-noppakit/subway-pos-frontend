import { Inter } from "next/font/google";
import "@/app/style/globals.css"
const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({ children }) {
  return (
    <html lang="en" className="no-scroll-login">
      <body className={inter.className}>{children}</body>
    </html>
  );
}
