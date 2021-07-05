# BetterCore
Need more helper classes? Check our [helper repository (BetterPlugins devs only)](https://github.com/BetterPluginsSpigot/ProjectBoardHelper)!
Some core classes that can be used in multiple of our projects

Get it through Maven:
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
  
<dependencies>
    <dependency>
        <groupId>com.github.betterpluginsspigot</groupId>
        <artifactId>bettercore</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
  
  <!-- Prevent compatibility issues with other BetterX plugins -->
  <build>
    <!-- Shade BetterYAML into your jar-->
   <plugins>
      <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-shade-plugin</artifactId>
         <version>3.2.1</version>
         <configuration>
            <!-- Only include required classes -->
            <minimizeJar>true</minimizeJar>
            <!-- Specify the paths to be relocated -->
            <relocations>
               <relocation>
                  <pattern>be.betterplugins.core</pattern>
                  <shadedPattern>YOUR.UNIQUE.PACKAGE.NAME.HERE</shadedPattern>
               </relocation>
            </relocations>
         </configuration>
         <executions>
            <execution>
               <phase>package</phase>
               <goals>
                  <goal>shade</goal>
               </goals>
            </execution>
         </executions>
      </plugin>
   </plugins>
</build>
```
