# Net-N-Play
[![Release](https://jitpack.io/v/vladocc/net-n-play.svg)](https://jitpack.io/vladocc/net-n-play)   
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/powered-by-black-magic.svg)](https://forthebadge.com)   
Net'n'Play is high-level general-purpose networking library for JVM written in Kotlin (with a bit of Java). With the main focus around the idea of Plug'n'Play-like experience, while keeping everything fool- and attack- proof.   

This library was initialy based on the networking code of the game that you can find [here](https://github.com/VladoCC/Pong-Online). For this reason, library is highly optimized to support client-server interactions in games.   

Idea behind it is simple: to serialize command that you want to send and it's class. Send the text through the network. Restore command using it's class and serialized data to fill the fields.

## Table of content
1. [Features of this approach](#features-of-this-approach)
2. [Download](#download)
3. [Documentation and examples](#documentation-and-examples)
4. [How to use](#how-to-use)

### Features of this approach
* Sender knows all the possible command types they can send.
* Sender has all the information about the data they need to supply to successfully execute code on the other side
* Even if data isn't correct, you don't need to check for it. Deserialization and casting to the class will do that for you.
* There is no way to call the command if it's not in the classpath on the receiving side of the network. This means that protecting system from attacks is as easy as creating specific commands that won't backfire on you.

### Download
This project uses [Jitpack](https://jitpack.io/) novel package repository technology of providing Git repositories as JVM libraries without dealing with all the difficulties of publishing. It helps to support fast update cycle and absolutely unintrusive for useres.

Gradle:
```gradle
  repositories {
        jcenter()
        maven { url "https://jitpack.io" }
   }
   dependencies {
         implementation 'com.github.vladocc:net-n-play:1.2'
   }
```

### Documentation and examples
[Video explaning most of the details about this library](https://youtu.be/LAFXXTrIsrg)
**More coming soon!**   
At this point the project is fully functional and can be used in your project, but it's not really documented yet.

### How to use
**Main concepts:**   
There are 2 main concepts you need to know of to understand how to use this library.
1. `Command`: the way to execute code on the other end of the network. Command is a class, which consist of one method `Command.execute(context)`. Key feature of this class is that it can be sent through the network in serialized for, then reconstructed and executed on the other side. There are two types of commands: `RequestCommand`, which goes from the client to the server and `ResponseCommand`, which goes back from the server to the client.    
Important thing about `RequestCommand` is that it also has `RequestCommand.getEngineType()` method. And server won't execute your command if it's returning an engine that you don't have access to.
2. `Engine`: class intended to sandbox an executing command from outside world on the server. Key feature of the engines is that they let you keep your server protected even if you need to execute some unsanitized code.Also. reroting features let you manage access of client to the data on your server and change this access rights on the fly. 

**Server:**   
You can initialize server using `ServerApplication` class or `ServerApplication.Builder` class for ease of setup.   
You can customize your `ServerAppliation` through builder by adding additional `CommandRouters` and `Processors`.   
But the only thing that you actually need to supply is at least one `AbstractEngine` of any type and then use this type as default engine type on build function `ServerAppliction.Builder.build(type)`.   
All the clients will be set to be serverd by this engine and the only way to access other engines is to call a `AbstractEngine.reroute(newType)` command and supply new engine type. If client is allowed to move to it (depends on `CommandRouter`'s `canReroute()` function) and engine of this type is actually presented in your `ServerApplication`, this client will get access to a new engine, losing access to the last one at the same time.

**Client:**  
Client initialization is very easy. You just need to create `NetworkConnector`, supply it with all the data that it needs to connect to server and also supply it with execution context.   
Execution context is just any object that you want to supply to all the `ResponseCommand`s that you'll receive from the server. It lets command to get access to important entry points of your client and notify it about received information.   
The last thing you need to do is start your connector by calling `NetworkConnector.start()`.   
After that you can interact with server by sending your `RequestCommands` through `NetworkConnector.send(command)` function.
