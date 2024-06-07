export default function DashboardLayout({ children }) {
    return (
        <>
            <div>header</div>
            <section>{children}</section>
            <div>footer</div>
        </>
    )
  
  }