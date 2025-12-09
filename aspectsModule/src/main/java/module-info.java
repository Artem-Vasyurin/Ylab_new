module aspectsModule {
    exports annotations;
    requires spring.context;
    requires spring.beans;
    requires spring.aop;
    requires org.aspectj.weaver;
    requires java.base;
    requires static lombok;
    requires spring.boot.autoconfigure;
    requires org.slf4j;
    requires jakarta.servlet;


}