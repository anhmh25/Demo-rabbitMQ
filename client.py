import requests
import threading
import time

### Fix it !!!
url = 'http://localhost:8081/api/v1/message/default-exchange'

def make_request(c):
    # Data to send in the POST request (as a dictionary)
    ### Fix it !!!
    data = {
      "message": f"message {c}"
    }

    # Convert the data to JSON format (if needed)
    # json_data = json.dumps(data)

    # Define the headers (if needed)
    headers = {
        "Content-Type": "application/json",  # Adjust the content type as required
        "Authorization": "Bearer your_token"  # Include authorization headers if needed
    }
    try:
        response = requests.post(url, json=data, headers=headers)
        print(f"Status code: {response.status_code}, Message: {response.json()}")
    except requests.RequestException as e:
        print(f"Request failed: {e}")

def send_requests(rate_per_second, duration_seconds):
    start_time = time.time()
    count = 0
    while True:
        count += 1
        # Start a new thread for each request
        threading.Thread(target=make_request(count)).start()

        # Sleep to achieve the desired request rate
        time.sleep(1 / rate_per_second)

        # Check if the specified duration has passed
        if time.time() - start_time > duration_seconds:
            break
        
        print(count)

# Example usage: Send 100 requests per second for 10 seconds
### Can fix it !!!
send_requests(rate_per_second=100, duration_seconds=10)