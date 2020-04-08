# gRPC

## gRPC란?

gRPC를 사용하면 클라이언트 프로그램이 마침 다른 컴퓨터에 있는 서버 프로그램의 로컬 객체처럼 메소드를 호출할 수 있다.
gRPC는 서비스 정의 개념을 기반으로 Parameter와 Return 유형으로 메소드를 원격에서 호출 할 수 있도록 지정한다.
서버 측에서는 클라이언트에 제공할 인터페이스(메소드)를 구현하고 클라이언트의 호출을 처리할 수 있는
gRPC 서버를 실행한다. 클라이언트는 서버와 같은 메소드를 제공하는 stub이라는 것을 가지고 있다.

![](https://imgur.com/xZwFefp.png)

gRPC는 Protocol Buffer(이하 ProtoBuf)라는 IDL(Interface Definition Language)를 사용하기 때문에 서버와 클라이언트 언어 선택이 자유롭다.

## 기본 예제

기본적으로 gRPC는 ProtoBuf를 사용한다. ProtoBuf를 통해서 메소드의 반환형과 매개변수를 정의한다.

```proto
syntax = "proto3";
// greeter service 정의
service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// 유저의 이름을 포함한 요청 메시지를 정의했다.
message HelloRequest {
  string name = 1;
}

// 인사를 포함한 응답 메시지를 정의했다.
message HelloReply {
  string message = 1;
}
```

## Service definition

다른 RPC system 처럼 서비스 정의 개념을 기반으로 매개 변수 및 리턴 타입으로 원격 호출을 할 수 있는 방법을 정의한다.

```proto
syntax = "proto3";
// greeter service 정의
service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// 유저의 이름을 포함한 요청 메시지를 정의했다.
message HelloRequest {
  string name = 1;
}

// 인사를 포함한 응답 메시지를 정의했다.
message HelloReply {
  string message = 1;
}
```

다음 4가지 종류의 서비스 메소드를 살펴보자.

- 클라이언트가 단일 함수를 서버에 보내고 단일 함수 호출과 같은 단일 응답을 받는 경우

```proto
rpc SayHello(HelloRequest) returns (HelloResponse);
```

- 클라이언트가 서버에 요청을 보내고 스트림을 가져와서 일련의 메시지를 다시 읽는 서버 스트리밍 RPC. 클라이언트는 이 스트림을 더 이상 메시지가 없을 때까지 읽는다. 
gRPC는 개별 RPC 호출 내에서 메시지 순서를 보장한다.

```proto
rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);
```

- 클라이언트 스트리밍 RPC로 클라이언트가 일련의 메시지를 작성하고 제공된 스트림을 사용하여 서버로 보낸다. 클라이언트가 메시지 쓰기를 마치면 서버가 메시지를 읽고 응답을 반환할 때까지
기다린다. gRPC는 개별 RPC 호출 내에서 메시지 순서를 보장한다.

```proto
rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);
```

- 양방향 스트림 RPC는 양쪽에서 읽기 / 쓰기 스트림을 사용하여 일련의 메시지를 보낸다. 

```proto
rpc BidiHello(stream HelloRequest) returns (stream HelloResponse);
```

## RPC Life Cycle

gRPC 타입에 따라 어떻게 동작하는지 알아보자.

### Unary RPC

하나의 요청과 하나의 응답을 보내는 경우다.

1. 클라이언트가 stub method를 호출하면, 서버는 호출한 클라이언트의 메타 데이터, 메소드 이름 및 접속 기한을 알린다.
2. 서버는 응답 전에 자신의 초기 메타 데이터를 즉시 보내거나 클라이언트의 요청을 받을 수 있다. 둘 중 어느 것이 먼저 되냐는 응용프로그램에 따라 다르다.
3. 서버에 클라이언 요청이 있으면 응답을 위한 작업을 수행한다. 응답은 상태 코드와 선택적 상태 메시지 및 선택적 후행 메타 데이터와 함께 클라이언트에 리턴된다.
4. OK 응답을 받으면 클라이언트는 응답 메시지를 받는다.

### Server Streaming RPC

Server streaming RPC는 서버가 클라이언트 요청에 응답하여 메시지 스트림을 반환한다는 점을 제외하면 Unary RPC와 유사하다. 

### Client Streaming RPC

Client Streaming RPC 클라이언트가 요청을 스트림을 전송한다는 점을 제외하면 Unary RPC와 유사하다.

### Bidirectional Streaming RPC

Bidirectional Streaming RPC는 클라이언트가 메소드를 호출하고 서버가 클라이언트 메타 데이터, 메소드 이름 및 최종 기한을 수신하면서 시작한다.
서버는 초기 메타 데이터를 다시 보내거나 클라이언트가 메시지 스트리밍을 할 때까지 기다릴 수 있다.

클라이언트가 보내는 스트림과 서버가 보내는 스트림은 독립적이므로 클라이언트와 서버는 순서에 상관없이 메시지를 읽고 쓸 수 있다.
예를 들어, 서버는 메시지를 작성하기 전에 클라이언트의 모든 메시지를 수신할 때까지 기다리거나
서버와 클라이언트가 "핑퐁"을 재생할 수 있다. 서버는 요청을 받고 응답을 보낸 다음 클라이언트가 요청을 다시 보낼 수 있다.

## Deadlines/Timeouts

gRPC를 사용하면 클라이언트는 **DEADLINE_EXCEEDED** 오류로 RPC가 완료되기를 기다리는 시간을 지정할 수 있다.
서버 측에서 서버는 특정 RPC가 시간 초과되었는지 또는 RPC를 완료하는데 얼마나 많은 시간이 남아 있는지 확인하기 위해 쿼리 할 수 있다.

최종 기한 또는 시간 초과를 지정하는 것은 언어 따라 다르다. 일부 언어 API는 시간 초과로 작동하며 일부 언어 API는 기한으로
작동한다. 또 기본 최종 기한을 갖거나 갖지 않을 수 있다.

## RPC termination

gRPC에서 클라이언트와 서버는 모두 호출 성공을 독립적으로 할 수 있다.
통신의 성공을 각각의 로컬에서 결정하기 때문이다. 예를 들어서 서버 측에서 응답을 모두 보내 성공으로 처리하고
클라이언트는 최종 기한 내에 못 받아서 실패 처리할 수 있다.

## Cancelling an RPC

클라이언트 또는 서버는 언제든지 RPC를 취소할 수 있다. 취소하면 RPC가 즉시 종료되므로 더 이상 작업하지 않는다.

## Metadata

메타 데이터는 키 값이 문자열, 값이 문자열인 것이 일반적이다. 값을 이진 데이터 일수도 있고 키-값 쌍 목록 형식의 특정 RPC 호출에 대한 정보다.
메타데이터는 gRPC 자체에 대해 불투명하다. 즉, 클라이언트는 호출과 관련된 정보를 서버에 제공하거나 그 반대로 제공할 수 있다.

## Channels

gRPC 채널은 지정된 호스트 및 포트에서 gRPC 서버에 대한 연결을 제공한다.
클라이언트 스텁을 작성할 때 사용된다. 고객은 메시지 압축을 켜거나 끄는 것과 같이 gRPC의 기본 동작을 수정하기 위해 채널
인수를 지정할 수 있다. 채널은 연결 및 유휴 상태를 포함하여 상태를 갖는다.

gRPC가 채널 폐쇄를 처리하는 방법은 언어에 따라 다르다. 일부 언어는 채널 상태 조회를 허용한다.