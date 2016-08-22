 Storm原理
Storm是一套分布式的、可靠的，可容错的用于处理流式数据的系统。处理工作会被委派给不同类型的组件，每个组件负责一项简单的、特定的处理任务。Storm集群的输入流由名为spout的组件负责。Spout将数据传递给名为bolt的组件，后者将以某种方式处理这些数据。例如bolt以某种存储方式持久化这些数据，或者将它们传递给另外的bolt。你可以把一个storm集群想象成一条由bolt组件组成的链，每个bolt对spout暴露出来的数据做某种方式的处理。
技术架构：
在storm集群中，结点被一个持续运行的主结点管理。
 
Storm集群中有两种结点：主结点和工作结点。主结点运行一个叫做Nimbus的守护进程，它负责在集群内分发代码，为每个工作结点指派任务和监控失败的任务，这个很类似于Hadoop中的Job Tracker。工作结点运行一个叫做Supervisor的守护进程，每个工作节点都是topology中一个子集的实现。Storm中的topology运行在不同机器的许多工作结点上。
 
zookeeper是完成nimbus和supervisor之间协调的服务。
 
关键组件
       Topology（拓扑） ：storm中运行的一个实时应用程序，因为各个组件间的消息流动形成逻辑上的一个拓扑结构。 Topolog是y一组由Spouts（数据源）和Bolts（数据操作）通过Stream Groupings进行连接组成的图。
 
 
 
Tuple：一次消息传递的基本单元。本来应该是一个key-value的map，但是由于各个组件间传递的tuple的字段名称已经事先定义好，所以tuple中只要按序填入各个value就行了，所以就是一个value list.
 
Stream：以tuple为单位组成的一条有向无界的数据流。
 
       Spout:从来源处读取数据并放入topology。Spout分成可靠和不可靠两种；当Storm接收失败时，可靠的Spout会对tuple（元组，数据项组成的列表）进行重发；而不可靠的Spout不会考虑接收成功与否只发射一次。而Spout中最主要的方法就是nextTuple（），该方法会发射一个新的tuple到topology，如果没有新tuple发射则会简单的返回。
        
 
         Bolt：Topology中所有的处理都由Bolt完成。Bolt可以完成任何事，比如：连接的过滤、聚合、访问文件、数据库等等。Bolt从Spout中接收数据并进行处理，如果遇到复杂流的处理也可能将tuple发送给另一个Bolt进行处理。而Bolt中最重要的方法是execute（），以新的tuple作为参数接收。不管是Spout还是Bolt，如果将tuple发射成多个流，这些流都可以通过declareStream（）来声明。
 
 
      
 
         Stream Groupings（流分组）: Stream Grouping定义了一个流在Bolt任务间该如何被切分，说白了就是谁来处理哪些数据，按照什么规则来分配 。
 
这里有Storm提供的6个Stream Grouping类型：
 
       1. 随机分组（Shuffle grouping）：随机分发tuple到Bolt的任务，保证每个任务获得相等数量的tuple。
 
       2. 字段分组（Fields grouping）：根据指定字段分割数据流，并分组。例如，根据“user-id”字段，相同“user-id”的元组总是分发到同一个任务，不同“user-id”的元组可能分发到不同的任务。
 
       3. 全部分组（All grouping）：tuple被复制到bolt的所有任务。这种类型需要谨慎使用。
 
       4. 全局分组（Global grouping）：全部流都分配到bolt的同一个任务。明确地说，是分配给ID最小的那个task。
 
 
       5. 无分组（None grouping）：你不需要关心流是如何分组。目前，无分组等效于随机分组。但最终，Storm将把无分组的Bolts放到Bolts或Spouts订阅它们的同一线程去执行（如果可能）。
       6. 直接分组（Direct grouping）：这是一个特别的分组类型。元组生产者决定tuple由哪个元组处理者任务接收。
特点
*            适用场景广泛
      可以用来处理消息和更新数据库(消息流处理),  对一个数据量进行持续的查询并返回客户端（持续计算）， 对一个耗资源的查询作实时并行化的处理(分布式方法调用）。
*         健壮性
      nimbus进程和supervisor都是快速失败（fail-fast)和无状态的。所有的状态存储于本地磁盘或Zookeeper上。杀死nimbus和supervisor进程后再重启，可以继续工作。
*         高可靠（消息处理）
       保证每个消息至少能得到一次完整处理。任务失败时，它会负责从消息源重试消息。
*         水平扩展（ supervisor ）
         计算是在多个线程、进程和服务器之间并行进行的。
*         容错性
         Storm会管理工作进程和节点的故障。处理过程中出现异常，会重新安排这个处理单元。Storm保证一个处理单元永远运行（除非你显式杀掉这个处理单元）。
*         快速
         系统的设计保证了消息能得到快速的处理，使用ZeroMQ作为其底层消息队列。在一个小集群中，每个结点每秒可以处理数以百万计的消息。
*         保证无数据丢失
         保证所有的数据被成功的处理。
*         简单的编程模型
         类似于MapReduce降低了并行批处理复杂性，Storm降低了进行实时处理的复杂性。
*         可以使用各种编程语言
         默认支持Clojure、Java、Ruby和Python。要增加对其他语言的支持，只需实现一个简单的Storm通信协议即可。
*         本地模式
         Storm有一个“本地模式”，可以在处理过程中完全模拟Storm集群。这让你可以快速进行开发和单元测试。
一些问题/缺点
*         Nimbus单点
*         Topology不支持动态部署
*         跨topology的bolt无法复用
*         Stream在Topology之间是无法流动的
*         不提供消息接入模块
*         安全性－只要有storm集群客户端，就能够提交作业。
应用场景
*         信息流处理{Stream processing}
    Storm可用来实时处理新数据和更新数据库，兼具容错性和可扩展性。
*         连续计算{Continuous computation}
    Storm可进行连续查询并把结果即时反馈给客户端。比如把Twitter上的热门话题发送到浏览器中。
*         分布式远程过程调用{Distributed RPC}
    Storm可用来并行处理密集查询。Storm的拓扑结构是一个等待调用信息的分布函数，当它收到一条调用信息后，会对查询进行计算，并返回查询结果。举个例子Distributed RPC可以做并行搜索或者处理大集合的数据。  
计算模式
流聚合（stream join）：是指将具有共同元组（tuple）字段的数据流（两个或者多个）聚合形成一个新的数据流的过程。
 
 
 
TOP N：对流式数据进行所谓“streaming top N”的计算，它的特点是持续的在内存中按照某个统计指标（如出现次数）计算TOP N，然后每隔一定时间间隔输出实时计算后的TOP N结果。
流式数据的TOP N计算的应用场景很多，例如计算twitter上最近一段时间内的热门话题、热门点击图片等等。
 
 
 
TimeCacheMap：Storm中使用一种叫做TimeCacheMap的数据结构，用于在内存中保存近期活跃的对象，它的实现非常地高效，而且可以自动删除过期不再活跃的对象。
 
简单ETL：Storm中的很多Bolt都有一个最常见的处理步骤：
1、读入一个tuple；
2、根据这个输入tuple，提取后发射0个，1个或多个tuple；
3、最后，确认这个tuple被成功处理。
按照上述处理步骤，依次处理发向这个Bolt的各个tuple元组。
这种模式可以实现像ETL这类的简单函数或过滤器功能，Storm中专门为这种模式封装了相应接口：IBasicBolt。BaseBasicBolt等类实现了这一接口。
 
批处理：批量一起处理一定数量的tuple元组，而不是每接收一个tuple就立刻处理一个tuple，这样可能是性能的考虑，或者是具体业务的需要。
         例如，批量查询或者更新数据库，如果每一条tuple生成一条sql执行一次数据库操作，数据量大的时候，效率会比批量处理的低很多，影响系统吞吐量。
         当然，如果要使用Storm的可靠数据处理机制的话，应该使用容器将这些tuple的引用缓存到内存中，直到批量处理的时候，ack这些tuple。
 
分布式RPC（DRPC） ：用于对Storm上大量的函数调用进行并行计算过程。对于每一次函数调用，Storm集群上运行的拓扑接收调用函数的参数信息作为输入流，并将计算结果作为输出流发射出去。
DRPC本身算不上Storm的特性，它是通过Storm的基本元素：streams，spouts，bolts，topologies而衍生的一个模式。DRPC可以单独作为一个独立于Storm的库发布，但由于其重要性还是和Storm捆绑在了一起。
DRPC通过DRPC Server来实现，DRPC Server的整体工作过程如下：
1、接收到一个RPC调用请求；
2、发送请求到Storm上的拓扑；
3、从Storm上接收计算结果；
4、将计算结果返回给客户端。
 
DRPC内部工作流程如下：
 
 
Client向DRPC Server发送被调用执行的DRPC函数名称及参数。
Storm上的topology通过DRPCSpout实现这一函数，从DPRC Server接收到函数调用流；
DRPC Server会为每次函数调用生成唯一的id；
Storm上运行的topology开始计算结果，最后通过一个ReturnResults的Bolt连接到DRPC Server，发送指定id的计算结果；
DRPC Server通过使用之前为每个函数调用生成的id，将结果关联到对应的发起调用的client，将计算结果返回给client。
 
其实即使不通过DRPC，而是通过在Topology中的spout中建立一个TCP/HTTP监听来接收数据，在最后一个Bolt中将数据发送到指定位置也是可以的。而DPRC则是Storm提供的一套开发组件，使用DRPC可以极大的简化这一过程。
Storm提供了一个topology builder——LinearDRPCTopologyBuilder，它可以自动完成几乎所有的DRPC步骤。包括：
构建spout；
向DRPC Server返回结果；
为Bolt提供函数用于对tuples进行聚集。