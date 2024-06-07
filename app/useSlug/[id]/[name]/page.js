

export default function page({ params }) {
    const { id, name } = params
    return (
        <div>
            <div>use slug</div>
            <div>
                <div>
                    {id}
                </div>
                <div>{name}</div>
            </div>
        </div>
    )
}
