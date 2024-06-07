export async function GET(req, { params }) {
    console.log({ req })
    console.log({ params })
    return Response.json({
        message: `Hello World ${params.id}`,
    });
}

export async function POST(req) {
    const body = await req.json();
    return new Response(JSON.stringify({ message: 'Data received successfully', data: body }), {
        status: 200,
        headers: {
            'Content-Type': 'application/json'
        }
    });
}