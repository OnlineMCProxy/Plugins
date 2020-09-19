let webSocket;

function connect(username) {
    if (webSocket != null && webSocket.readyState !== WebSocket.CLOSED && webSocket.readyState !== WebSocket.CLOSING) {
        return;
    }

    webSocket = new WebSocket("ws://${host}/ws");
    webSocket.onopen = ev => {
        webSocket.send(username);
        this.clearChat();
        console.log("Connection opened")
    };

    webSocket.onmessage = ev => {
        let input = ev.data;
        try {

            let json = JSON.parse(input);

            this.setInfo(json.success, json.message);

        } catch (e) {
            this.displayMessage(input);
        }
    }

    webSocket.onclose = ev => {
        console.log("Connection closed");
        webSocket = null;
    }

}

function sendMessage(message) {
    if (webSocket == null) {
        return;
    }

    webSocket.send(message);
}


function displayMessage(message) {
    let element = document.createElement("a");
    element.innerHTML = message;

    let base = document.getElementById("messages");
    base.append(document.createElement("br"));
    base.append(element);

    element.scrollIntoView();
}

function setInfo(success, message) {
    let successElement = document.getElementById("info-success");
    let messageElement = document.getElementById("info-message");

    successElement.innerText = success ? "Success" : "Failed";
    successElement.style.color = success ? "lightgreen" : "red";

    messageElement.innerText = message;

    setTimeout(() => {
        successElement.innerText = "";
        messageElement.innerText = "";
    }, 5000);

}

function clearChat() {
    this.clear(document.getElementById('messages'));
}

function clear(node) {
    console.log("test")
    while (node.firstChild) {
        console.log(node.firstChild);
        node.removeChild(node.firstChild);
    }
}
