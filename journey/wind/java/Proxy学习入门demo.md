public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static T getProxy(final T obj) {

        return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                System.out.println("factory before");
                Object invoke = method.invoke(obj, args);
                System.out.println("factory after");
                return invoke;
            }

        });
    }
}


public class HelloHandler implements InvocationHandler{
   
    private Object obj;
   
    public HelloHandler(Object obj) {
        super();
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        before();
        Object invoke = method.invoke(obj, args);
        after();
       
        return invoke;
    }

    private void after() {
        System.out.println("after");
    }

    private void before() {
        System.out.println("before");
    }

}

public class ProxyTest {

    public static void main(String[] args) {
        IHello hello = new HelloImpl();
        InvocationHandler helloHandler = new HelloHandler(hello);
       
        //IHello proxy = (IHello) Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), helloHandler);
        IHello proxy = (IHello)ProxyFactory.getProxy(hello);
        proxy.sayHello("good bye");
    }

}