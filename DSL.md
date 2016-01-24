## Introduction ##


&lt;TODO&gt;



## Byte Code Manipulation ##

One important aspect related to the implementation of the DSL, is that it makes use of bytecode manipulation (using cglib).
This makes possible to define behavior for the FSM this way:

```

final RadioStateMachine radioStateMachinePrototype = getPrototype();

in(State.Off).on(Event.TogglePower).goTo(State.On).execute(radioStateMachinePrototype.logTransitionFromOffToOn());
in(State.Off).executeOnEntry(radioStateMachinePrototype.logOffEntry());
```

We create a Proxy for the State Machine using getPrototype()


```


protected TStateMachine getPrototype() {
return (TStateMachine) CallInterceptorBuilder.build(createStateMachine(null).getClass());
}

public static <T> T build(final Class<T> type) {
final MethodInterceptor interceptor = new ClassMethodCallInterceptor<T>();
final Enhancer enhancer = new Enhancer();
enhancer.setSuperclass(type);
enhancer.setCallback(interceptor);
final T proxy = (T) enhancer.create();
return proxy;
}

```

Now this proxy intercepts every function call and pushes it in a Stack:

```

@Override
public Object intercept(final Object object, final Method method, final Object[] args, final MethodProxy methodProxy) throws Throwable {
if (!method.isAccessible()) {
method.setAccessible(true);
}
final MethodCall<TObject> methodCall = new ClassMethodCallImpl<TObject>(method, args);
CallInterceptorBuilder.push(methodCall);
return null;
}
```

And we pop it from the Stack:

```

@Override
public ExecuteSyntax<TStateMachine, TState, TEvent> execute(
final Object methodCall) {
final MethodCall<TStateMachine> call = CallInterceptorBuilder.pop();
LOG.debug(currentTransition.toString() + " use action " + call);
this.currentTransition.getActions().add(
new MethodCallAction<TStateMachine, TState, TEvent>(call));
return this;
}
```



So look what happens when we execute:

```

in(State.Off).on(Event.TogglePower).goTo(State.On).execute(radioStateMachinePrototype.logTransitionFromOffToOn());


/** This happens:

radioStateMachinePrototype.logTransitionFromOffToOn() => Stack.push(MethodCall);
execute() => actionToBeExecuted = Stack.pop();

**/

```

## Limitations ##

So, now let's see the implications for the DSL of using bytecode manipulation in this way.

We define this easy behavior:

```

in(State.first).executeOnExit(stateMachinePrototype.log("1:exiting..."));
in(State.first).executeOnEntry(stateMachinePrototype.log("2:entering..."));
```

so the output of the FSM at runtime, when State.first is first entered and then exited looks like

```

"2:entering..."
"1:exiting..."
```

which is completely normal and expected.

But now we model the same behavior in a different way:

```

Void exit = stateMachinePrototype.log("1:exiting...");
Void enter = stateMachinePrototype.log("2:entering...");
in(State.first).executeOnExit(exit);
in(State.first).executeOnEntry(enter);
```

We  expect to have the same output as before, but instead we get

```

"1:exiting..."
"2:entering..."
```

which is annoying and not a "correct behavior". Why:

In the first case what happens is:
```

//in(State.first).executeOnExit(stateMachinePrototype.log("1:exiting..."));
//in(State.first).executeOnEntry(stateMachinePrototype.log("2:entering..."));

stateMachinePrototype.log("1:exiting...") => push()
executeOnExit() => pop()
stateMachinePrototype.log("2:entering...") => push()
executeOnEntry() => pop()

And the Stack sequence looks like

push(stateMachinePrototype.log("1:exiting..."))
exitAction = pop()
push(stateMachinePrototype.log("2:entering..."))
entryAction = pop()
```


In the second case what happens is:

```

//Void exit = stateMachinePrototype.log("1:exiting...");
//Void enter = stateMachinePrototype.log("2:entering...");
//in(State.first).executeOnExit(exit);
//in(State.first).executeOnEntry(enter);

stateMachinePrototype.log("1:exiting...") => push()
stateMachinePrototype.log("2:entering...") => push()
executeOnExit() => pop()
executeOnEntry() => pop()

And the Stack sequence looks like

push(stateMachinePrototype.log("1:exiting..."))
push(stateMachinePrototype.log("2:entering..."))
exitAction = pop() -> stateMachinePrototype.log("2:entering...") !!!!!!!!!
entryAction = pop() -> stateMachinePrototype.log("1:exiting...") !!!!!!!!!
```



So keep it in mind when using the DSL.

