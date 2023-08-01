import java.lang.management.{ManagementFactory, MemoryMXBean}
import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}

class JMXConnectors extends App {
  // URL
  val jmxURLzkpr = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:9999/jmxrmi")
  val jmxURLkafka = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:9998/jmxrmi")

  // Connections
  val jmxZooConn = JMXConnectorFactory.connect(jmxURLzkpr, null)
  val jmxKafkaConn = JMXConnectorFactory.connect(jmxURLkafka, null)
  val connectionZK = jmxZooConn.getMBeanServerConnection
  val connectionKafka = jmxKafkaConn.getMBeanServerConnection

  // ???
  val memProxyZK = ManagementFactory.newPlatformMXBeanProxy(connectionZK, ManagementFactory.MEMORY_MXBEAN_NAME, classOf[MemoryMXBean])
  val memProxyKafka = ManagementFactory.newPlatformMXBeanProxy(connectionKafka, ManagementFactory.MEMORY_MXBEAN_NAME, classOf[MemoryMXBean])
}
