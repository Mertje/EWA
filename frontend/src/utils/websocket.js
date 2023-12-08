import webstomp from 'webstomp-client';

export default class socket {

    connected = false;
    subscribtion;

    constructor() {
        this.client = webstomp.client("ws://localhost:8083/websocket");
        return this;
    }

    connect() {
        this.client.connect({}, () => {
            this.connected = true;
            console.log("Connected to the socket");
        });
        return this;
    }

    subscribe(destination, callback) {
        if (!this.connected) {
            throw new Error("Not connected to the socket");
        }
        this.subscribtion = this.client.subscribe(destination, callback);
    }

    send(destination, body) {
        if (!this.connected) {
            throw new Error("Not connected to the socket");
        }
        this.client.send(destination, body);
    }

    disconnect() {
        if (!this.connected) {
            throw new Error("Not connected to the socket");
        }
        if (this.subscribtion) {
            this.subscribtion.unsubscribe();
        };

        this.client.disconnect(() => {
            this.connected = false;
            console.log("Disconnected from the socket");
        });
    }
}