<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blood Bank - Save Lives</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .hero-section {
            text-align: center;
            color: white;
            margin-bottom: 3rem;
        }
        .hero-section h1 {
            font-size: 3.5rem;
            font-weight: 700;
            margin-bottom: 1rem;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        .hero-section p {
            font-size: 1.3rem;
            opacity: 0.95;
        }
        .choice-card {
            background: white;
            border-radius: 20px;
            padding: 3rem 2rem;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            transition: all 0.3s ease;
            height: 100%;
            text-align: center;
        }
        .choice-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 50px rgba(0,0,0,0.3);
        }
        .choice-card .icon-wrapper {
            width: 120px;
            height: 120px;
            margin: 0 auto 2rem;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 4rem;
        }
        .donor-card .icon-wrapper {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        .receiver-card .icon-wrapper {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }
        .choice-card h3 {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 1rem;
            color: #333;
        }
        .choice-card p {
            color: #666;
            font-size: 1.1rem;
            margin-bottom: 2rem;
        }
        .choice-card .btn {
            padding: 0.8rem 2.5rem;
            font-size: 1.1rem;
            font-weight: 600;
            border-radius: 50px;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: all 0.3s ease;
        }
        .donor-card .btn {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            border: none;
            color: white;
        }
        .donor-card .btn:hover {
            transform: scale(1.05);
            box-shadow: 0 5px 20px rgba(245, 87, 108, 0.4);
        }
        .receiver-card .btn {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            border: none;
            color: white;
        }
        .receiver-card .btn:hover {
            transform: scale(1.05);
            box-shadow: 0 5px 20px rgba(79, 172, 254, 0.4);
        }
        .stats-section {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 2rem;
            margin-top: 3rem;
            color: white;
        }
        .stat-item {
            text-align: center;
        }
        .stat-item h4 {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }
        .stat-item p {
            font-size: 1rem;
            opacity: 0.9;
        }
        @media (max-width: 768px) {
            .hero-section h1 {
                font-size: 2.5rem;
            }
            .choice-card {
                margin-bottom: 2rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="hero-section">
            <i class="bi bi-droplet-fill" style="font-size: 5rem; margin-bottom: 1rem;"></i>
            <h1>Blood Bank Management</h1>
            <p>Every donation counts. Every life matters.</p>
        </div>

        <div class="row justify-content-center g-4">
            <div class="col-md-5">
                <div class="choice-card donor-card">
                    <div class="icon-wrapper">
                        <i class="bi bi-heart-pulse-fill"></i>
                    </div>
                    <h3>Become a Donor</h3>
                    <p>Join our community of heroes and save lives by donating blood</p>
                    <a href="${pageContext.request.contextPath}/donors" class="btn">View Donors</a>
                </div>
            </div>
            <div class="col-md-5">
                <div class="choice-card receiver-card">
                    <div class="icon-wrapper">
                        <i class="bi bi-droplet-half"></i>
                    </div>
                    <h3>Need Blood</h3>
                    <p>Register as a receiver and get matched with compatible donors</p>
                    <a href="${pageContext.request.contextPath}/receivers" class="btn">View Receivers</a>
                </div>
            </div>
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>