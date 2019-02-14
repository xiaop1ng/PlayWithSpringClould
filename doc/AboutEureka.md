# 关于 Eureka

Eureka 是 Netflix 开源的一款提供服务注册和发现的产品，也就是注册中心的角色。

### 那么什么是注册中心呢？

注册中心的职责是管理各种服务功能包括服务的注册、发现、熔断、负载、降级等。也就是作用于形形色色的各种服务之间，帮助服务的发现与调用。

比喻：ServerA 调用 ServerB

ServerA -> ServerB

会变为：ServerA 先找到服务中心，然后再由服务中心找到 ServerB

ServerA -> 服务中心 -> ServerB

ServerA 调用 ServerB 后，ServerB 再调用 ServerC

ServerA -> ServerB -> ServerC

会变为：ServerA 先找到服务中心，然后再由服务中心找到 ServerB，之后 ServerB 找到服务中心，最后由服务中心找到 ServerC

ServerA -> 服务中心 -> ServerB
ServerB -> 服务中心 -> ServerC

### 关于 Netflix

关于大名鼎鼎的 Netflix ，是的没错就是影视作品公司 Netflix ，Netflix 拍摄的代表性的美剧有《纸牌屋》、《毒枭》、《怪奇物语》。

