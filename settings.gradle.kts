pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "bdui"
include(
    ":engine",
    ":parser",
    ":render:compose",
    ":component:button:compose",
    ":component:box:compose",
    ":component:column:compose",
    ":component:image:compose",
    ":component:input:compose",
    ":component:row:compose",
    ":component:switch:compose",
    ":component:text:compose",
    ":component:toolbar:compose",
    ":component:lazy_row:compose",
    ":component:lazy-column:compose",
    ":component:meta:compose",
    ":component:base:compose",
    ":component:common",
    ":function:base",
    ":handler:flow",
    ":interaction:base:flow",
    ":interaction:state-patch:flow",
    ":interaction:delay:flow",
    ":sample"
)