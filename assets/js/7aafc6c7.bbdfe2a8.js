"use strict";(self.webpackChunkcarpet_tis_addition_website=self.webpackChunkcarpet_tis_addition_website||[]).push([[847],{8939:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>o,contentTitle:()=>c,default:()=>a,frontMatter:()=>d,metadata:()=>s,toc:()=>t});const s=JSON.parse('{"id":"loggers","title":"Loggers","description":"commandBlock","source":"@site/docs/loggers.md","sourceDirName":".","slug":"/loggers","permalink":"/docs/loggers","draft":false,"unlisted":false,"editUrl":"https://github.com/TISUnion/Carpet-TIS-Addition/tree/master/website/docs/loggers.md","tags":[],"version":"current","sidebarPosition":2,"frontMatter":{"sidebar_position":2},"sidebar":"tutorialSidebar","previous":{"title":"Rules","permalink":"/docs/rules"},"next":{"title":"Commands","permalink":"/docs/commands"}}');var l=i(4848),r=i(8453);const d={sidebar_position:2},c="Loggers",o={},t=[{value:"commandBlock",id:"commandblock",level:2},{value:"damage",id:"damage",level:2},{value:"item",id:"item",level:2},{value:"lifetime",id:"lifetime",level:2},{value:"lightQueue",id:"lightqueue",level:2},{value:"memory",id:"memory",level:2},{value:"microTiming",id:"microtiming",level:2},{value:"mobcapsLocal",id:"mobcapslocal",level:2},{value:"movement",id:"movement",level:2},{value:"phantom",id:"phantom",level:2},{value:"raid",id:"raid",level:2},{value:"scounter",id:"scounter",level:2},{value:"ticket",id:"ticket",level:2},{value:"tickWarp",id:"tickwarp",level:2},{value:"turtleEgg",id:"turtleegg",level:2},{value:"wanderingTrader",id:"wanderingtrader",level:2},{value:"xcounter",id:"xcounter",level:2},{value:"xporb",id:"xporb",level:2}];function h(e){const n={a:"a",code:"code",em:"em",h1:"h1",h2:"h2",header:"header",li:"li",p:"p",strong:"strong",ul:"ul",...(0,r.R)(),...e.components};return(0,l.jsxs)(l.Fragment,{children:[(0,l.jsx)(n.header,{children:(0,l.jsx)(n.h1,{id:"loggers",children:"Loggers"})}),"\n",(0,l.jsx)(n.h2,{id:"commandblock",children:"commandBlock"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log commandBlock <option>"})}),"\n",(0,l.jsx)(n.p,{children:"Info when a command block or command block minecart executes command"}),"\n",(0,l.jsx)(n.p,{children:"It's useful to find out where the annoying hidden running command block is"}),"\n",(0,l.jsxs)(n.p,{children:["With default ",(0,l.jsx)(n.code,{children:"throttled"})," option every command block will log at the highest frequency once every 3 seconds"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"throttled"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"throttled"}),", ",(0,l.jsx)(n.code,{children:"all"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"damage",children:"damage"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log damage <selector>"})}),"\n",(0,l.jsx)(n.p,{children:"Info when a living entity gets damage and display how the damage gets calculated"}),"\n",(0,l.jsxs)(n.p,{children:["The ",(0,l.jsx)(n.code,{children:"<selector>"})," option consist of 1 or 2 object declaration strings\nand an optional ",(0,l.jsx)(n.code,{children:"->"})," or ",(0,l.jsx)(n.code,{children:"<->"})," direction indicator"]}),"\n",(0,l.jsxs)(n.p,{children:["Example structures for the ",(0,l.jsx)(n.code,{children:"<selector>"})," where ",(0,l.jsx)(n.code,{children:"A"})," and ",(0,l.jsx)(n.code,{children:"B"})," are 2 object declarations:"]}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"A"}),": damage source is ",(0,l.jsx)(n.code,{children:"A"})," or damage target is ",(0,l.jsx)(n.code,{children:"A"})]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"->A"}),": damage target is ",(0,l.jsx)(n.code,{children:"A"})]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"A->"}),": damage source is ",(0,l.jsx)(n.code,{children:"A"})]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"A->B"}),": damage source is ",(0,l.jsx)(n.code,{children:"A"}),", and damage target is ",(0,l.jsx)(n.code,{children:"B"})," (",(0,l.jsx)(n.code,{children:"A"})," just dealt some damages to ",(0,l.jsx)(n.code,{children:"B"}),")"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"A<->B"}),": damage source is ",(0,l.jsx)(n.code,{children:"A"})," and damage target is ",(0,l.jsx)(n.code,{children:"B"}),", or damage source is ",(0,l.jsx)(n.code,{children:"B"})," and damage target is ",(0,l.jsx)(n.code,{children:"A"})]}),"\n"]}),"\n",(0,l.jsx)(n.p,{children:"To declare an object, you can choose any of the following syntaxes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Hardcoded:","\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.em,{children:"empty"}),", ",(0,l.jsx)(n.code,{children:"*"})," or ",(0,l.jsx)(n.code,{children:"all"}),": Matches anything"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"me"}),": Matches the subscriber itself"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"players"}),": Matches player"]}),"\n"]}),"\n"]}),"\n",(0,l.jsxs)(n.li,{children:["Entity type (matches given type of entities):","\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"cat"}),": Matches cat"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"minecraft:cat"}),": same as above"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"entity_type/cat"}),": same as above"]}),"\n"]}),"\n"]}),"\n",(0,l.jsxs)(n.li,{children:["Damage name (matches source only):","\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"hotFloor"}),": Matches if damage msg ID is hotFloor (i.e. damage type ",(0,l.jsx)(n.code,{children:"minecraft:hot_floor"}),")"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"damage_name/hotFloor"}),": same as above"]}),"\n"]}),"\n"]}),"\n",(0,l.jsxs)(n.li,{children:["Damage type (available in mc1.19.4+, matches source only):","\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"hot_floor"}),": Matches if damage type is ",(0,l.jsx)(n.code,{children:"minecraft:hot_floor"})]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"minecraft:hot_floor"}),": same as above"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"damage_type/hot_floor"}),": same as above"]}),"\n"]}),"\n"]}),"\n",(0,l.jsxs)(n.li,{children:["Entity selector:","\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"@e[distance=..20]"}),": this works too, but requires permission level 2 like vanilla"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"Steve"}),": works if player Steve is online"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"some-uuid-string"}),": just like an entity selector in command"]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,l.jsxs)(n.p,{children:[(0,l.jsx)(n.code,{children:"<selector>"})," examples:"]}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"->me"}),": Damage dealt to the subscriber itself"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"->creeper"}),": Damage dealt to creeper"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"vex->"}),": Damage dealt from vex"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"zombie"}),": Damage from / to zombies"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"minecraft:zombie"}),": The same as ",(0,l.jsx)(n.code,{children:"zombie"})]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"me->zombie"}),": Damage from the subscriber to zombies"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"me<->zombie"}),": Damage between the subscriber and zombies"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"hotFloor->zombie"}),": Damage from magma block to zombies"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"->@e[distance=..10]"}),": Damage dealt to entities within 10m of the subscriber"]}),"\n"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"all"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"all"}),", ",(0,l.jsx)(n.code,{children:"players"}),", ",(0,l.jsx)(n.code,{children:"me"}),", ",(0,l.jsx)(n.code,{children:"->creeper"}),", ",(0,l.jsx)(n.code,{children:"vex->"}),", ",(0,l.jsx)(n.code,{children:"me->zombie"}),", ",(0,l.jsx)(n.code,{children:"hotFloor->zombie"}),", ",(0,l.jsx)(n.code,{children:"Steve"}),", ",(0,l.jsx)(n.code,{children:"@e[distance=..10]"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"item",children:"item"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log item <events>"})}),"\n",(0,l.jsx)(n.p,{children:"Info when something happens to an item entity, for example item despawned after 5min"}),"\n",(0,l.jsx)(n.p,{children:"Available events:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"create"}),": An item entity is created in the world for any kinds of reason. Stack trace is included in the message"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"die"}),": An item entity died"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"despawn"}),": An item entity despawned"]}),"\n"]}),"\n",(0,l.jsxs)(n.p,{children:["Use csv format, like ",(0,l.jsx)(n.code,{children:"despawn,die"})," for logging multiple events"]}),"\n",(0,l.jsxs)(n.p,{children:["Available option separators: ",(0,l.jsx)(n.code,{children:","}),", ",(0,l.jsx)(n.code,{children:"."})," and ",(0,l.jsx)(n.code,{children:" "})," (",(0,l.jsx)(n.code,{children:"."})," is the only choice in 1.14.4 version)"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"despawn"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"despawn"}),", ",(0,l.jsx)(n.code,{children:"die"}),", ",(0,l.jsx)(n.code,{children:"despawn,die"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"lifetime",children:"lifetime"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log lifetime <entity_type>"})}),"\n",(0,l.jsx)(n.p,{children:"A HUD Logger"}),"\n",(0,l.jsxs)(n.p,{children:["Displays the current lifetime statistic of specific entity type from the ",(0,l.jsx)(n.a,{href:"/docs/commands#lifetime",children:"LifeTime Tracker"})," in the dimension the player is in"]}),"\n",(0,l.jsx)(n.p,{children:"The logging options is required to be an available entity type"}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Default option: N/A"}),"\n",(0,l.jsx)(n.li,{children:"Suggested options: All available entity types in current's lifetime tracking"}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"lightqueue",children:"lightQueue"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log lightQueue"})}),"\n",(0,l.jsx)(n.p,{children:"A HUD logger for debugging light suppression. It displays the following information of the lighting task queue:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Average task accumulation speed"}),"\n",(0,l.jsxs)(n.li,{children:["Current light queue size. Indicating with symbol ",(0,l.jsx)(n.code,{children:"S"})]}),"\n",(0,l.jsxs)(n.li,{children:["Estimated duration of light suppression if the light suppressor is switched off now. Indicating with symbol ",(0,l.jsx)(n.code,{children:"T"})]}),"\n",(0,l.jsx)(n.li,{children:"Average task enqueuing speed"}),"\n",(0,l.jsx)(n.li,{children:"Average task executing speed"}),"\n"]}),"\n",(0,l.jsxs)(n.p,{children:["The sampling duration can be specified with rule ",(0,l.jsx)(n.a,{href:"/docs/rules#lightqueueloggersamplingduration",children:"lightQueueLoggerSamplingDuration"}),", default 60gt"]}),"\n",(0,l.jsxs)(n.p,{children:["Specify the logging option to select the world you want log its light queue, like ",(0,l.jsx)(n.code,{children:"/log mobcaps"})]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"dynamic"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"dynamic"}),", ",(0,l.jsx)(n.code,{children:"overworld"}),", ",(0,l.jsx)(n.code,{children:"the_nether"}),", ",(0,l.jsx)(n.code,{children:"the_end"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"memory",children:"memory"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log memory"})}),"\n",(0,l.jsx)(n.p,{children:"Display current memory usage of the server in HUD"}),"\n",(0,l.jsxs)(n.p,{children:["Format: ",(0,l.jsx)(n.code,{children:"Used memory"})," / ",(0,l.jsx)(n.code,{children:"Allocated memory"})," | ",(0,l.jsx)(n.code,{children:"Max memory"})]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Default option: N/A"}),"\n",(0,l.jsx)(n.li,{children:"Suggested options: N/A"}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"microtiming",children:"microTiming"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log microTiming <type>"})}),"\n",(0,l.jsx)(n.p,{children:"Log micro timings of redstone components. The ticket of the chunk the component is in needs to be at least lazy-processing (ticket level 32)"}),"\n",(0,l.jsxs)(n.p,{children:["Check rule ",(0,l.jsx)(n.a,{href:"/docs/rules#microtiming",children:"microTiming"})," for detail. Remember to use ",(0,l.jsx)(n.code,{children:"/carpet microTiming true"})," to enable logger functionality"]}),"\n",(0,l.jsx)(n.p,{children:"Available options:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"all"}),": Default value, log all events"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"merged"}),": Log all events and merged continuous same events"]}),"\n",(0,l.jsxs)(n.li,{children:[(0,l.jsx)(n.code,{children:"unique"}),": Log the first unique event in every gametick"]}),"\n"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"merged"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"all"}),", ",(0,l.jsx)(n.code,{children:"merged"}),", ",(0,l.jsx)(n.code,{children:"unique"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"mobcapslocal",children:"mobcapsLocal"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.strong,{children:"Available in Minecraft 1.18.2+"})}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log mobcapsLocal [<player>]"})}),"\n",(0,l.jsx)(n.p,{children:"A HUD Logger"}),"\n",(0,l.jsx)(n.p,{children:"Like carpet's mobcaps logger, but what it displays is the local mobcap of the specified player"}),"\n",(0,l.jsx)(n.p,{children:"If no player is specified, it will display the local mobcap of the subscriber"}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Default option: N/A"}),"\n",(0,l.jsx)(n.li,{children:"Suggested options: Names of all online players"}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"movement",children:"movement"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log movement <target>"})}),"\n",(0,l.jsxs)(n.p,{children:["Switch: rule ",(0,l.jsx)(n.a,{href:"/docs/rules#loggermovement",children:"loggerMovement"})]}),"\n",(0,l.jsx)(n.p,{children:"Info when a living entity tries to move and display how the actual movement gets calculated"}),"\n",(0,l.jsxs)(n.p,{children:[(0,l.jsx)(n.code,{children:"<target>"})," is an entity selector. Make sure you have selector the necessary targets or expect log spam (",(0,l.jsx)(n.code,{children:"@"})," selector requires permission level 2)"]}),"\n",(0,l.jsxs)(n.p,{children:["Additionally, appending ",(0,l.jsx)(n.code,{children:"non_zero:"})," as the prefix in the ",(0,l.jsx)(n.code,{children:"<target>"})," string will filter out those logs whose final movement vector is 0"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"non_zero:@a[distance=..10]"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"non_zero:@a[distance=..10]"}),", ",(0,l.jsx)(n.code,{children:"@s"}),", ",(0,l.jsx)(n.code,{children:"non_zero:@e[type=creeper,distance=..5]"}),", ",(0,l.jsx)(n.code,{children:"Steve"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"phantom",children:"phantom"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log phantom <options>"})}),"\n",(0,l.jsxs)(n.p,{children:["With option ",(0,l.jsx)(n.code,{children:"spawning"}),", it informs when someone spawns a wave of phantoms"]}),"\n",(0,l.jsxs)(n.p,{children:["With option ",(0,l.jsx)(n.code,{children:"reminder"}),", it reminds you when you haven't slept for 45min or 60min"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"spawning"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"spawning"}),", ",(0,l.jsx)(n.code,{children:"reminder"}),", ",(0,l.jsx)(n.code,{children:"spawning,reminder"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"raid",children:"raid"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log raid"})}),"\n",(0,l.jsx)(n.p,{children:"Info when these raid related events happen:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"A raid has been created"}),"\n",(0,l.jsx)(n.li,{children:"A raid has been invalidated"}),"\n",(0,l.jsx)(n.li,{children:"The bad omen level of a raid has been increased"}),"\n",(0,l.jsx)(n.li,{children:"The center Position of a raid has been moved"}),"\n"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Default option: N/A"}),"\n",(0,l.jsx)(n.li,{children:"Suggested options: N/A"}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"scounter",children:"scounter"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log scounter <color>"})}),"\n",(0,l.jsx)(n.p,{children:"It's a HUD logger"}),"\n",(0,l.jsxs)(n.p,{children:["Similar to carpet's ",(0,l.jsx)(n.code,{children:"counter"})," logger for its hopper counter, this logger is used for showing items output from infinity item supplier hoppers created by rule ",(0,l.jsx)(n.a,{href:"/docs/rules#hoppernoitemcost",children:"hopperNoItemCost"})]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Default option: N/A"}),"\n",(0,l.jsx)(n.li,{children:"Suggested options: All dye color names"}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"ticket",children:"ticket"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log ticket <types>"})}),"\n",(0,l.jsx)(n.p,{children:"Info when a ticket is created or removed"}),"\n",(0,l.jsxs)(n.p,{children:["Use csv format, like ",(0,l.jsx)(n.code,{children:"portal,dragon"})," for logging multiple types of ticket"]}),"\n",(0,l.jsxs)(n.p,{children:["Available option separators: ",(0,l.jsx)(n.code,{children:","}),", ",(0,l.jsx)(n.code,{children:"."})," and ",(0,l.jsx)(n.code,{children:" "})," (",(0,l.jsx)(n.code,{children:"."})," is the only choice in 1.14.4 version)"]}),"\n",(0,l.jsxs)(n.p,{children:[(0,l.jsx)(n.strong,{children:"Warning:"})," Logging ",(0,l.jsx)(n.code,{children:"unknown"})," ticket may make you get spammed"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"portal"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"portal,dragon"}),", ",(0,l.jsx)(n.code,{children:"start"}),", ",(0,l.jsx)(n.code,{children:"dragon"}),", ",(0,l.jsx)(n.code,{children:"player"}),", ",(0,l.jsx)(n.code,{children:"forced"}),", ",(0,l.jsx)(n.code,{children:"light"}),", ",(0,l.jsx)(n.code,{children:"portal"}),", ",(0,l.jsx)(n.code,{children:"post_teleport"}),", ",(0,l.jsx)(n.code,{children:"unknown"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"tickwarp",children:"tickWarp"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log tickWarp <option>"})}),"\n",(0,l.jsx)(n.p,{children:"A HUD logger to display to progress of current tick warping"}),"\n",(0,l.jsx)(n.p,{children:"It only shows up when the server is tick warping"}),"\n",(0,l.jsxs)(n.p,{children:["See ",(0,l.jsx)(n.a,{href:"/docs/commands#warp-status",children:"/tick warp status"})," command for displaying more details of tick warp"]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsxs)(n.li,{children:["Default option: ",(0,l.jsx)(n.code,{children:"bar"})]}),"\n",(0,l.jsxs)(n.li,{children:["Suggested options: ",(0,l.jsx)(n.code,{children:"bar"}),", ",(0,l.jsx)(n.code,{children:"value"})]}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"turtleegg",children:"turtleEgg"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log turtleEgg"})}),"\n",(0,l.jsx)(n.p,{children:"Logs when a turtle egg is trampled to broken"}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Default option: N/A"}),"\n",(0,l.jsx)(n.li,{children:"Suggested options: N/A"}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"wanderingtrader",children:"wanderingTrader"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log wanderingTrader"})}),"\n",(0,l.jsx)(n.p,{children:"Logs when someone summon (actually, special natural spawning based on randomly chosen player) a wandering trader"}),"\n",(0,l.jsx)(n.h2,{id:"xcounter",children:"xcounter"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log xcounter <color>"})}),"\n",(0,l.jsx)(n.p,{children:"It's a HUD logger"}),"\n",(0,l.jsxs)(n.p,{children:["Similar to carpet's ",(0,l.jsx)(n.code,{children:"counter"})," logger for its hopper counter, this logger is used for showing xp statistic xp counters created by rule ",(0,l.jsx)(n.a,{href:"/docs/rules#hopperxpcounters",children:"hopperXpCounters"})]}),"\n",(0,l.jsx)(n.p,{children:"Attributes:"}),"\n",(0,l.jsxs)(n.ul,{children:["\n",(0,l.jsx)(n.li,{children:"Default option: N/A"}),"\n",(0,l.jsx)(n.li,{children:"Suggested options: All dye color names"}),"\n"]}),"\n",(0,l.jsx)(n.h2,{id:"xporb",children:"xporb"}),"\n",(0,l.jsx)(n.p,{children:(0,l.jsx)(n.code,{children:"/log xporb <events>"})}),"\n",(0,l.jsxs)(n.p,{children:["Basically the same as ",(0,l.jsx)(n.a,{href:"#item",children:"item logger"})," but logs experience orb entities"]})]})}function a(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,l.jsx)(n,{...e,children:(0,l.jsx)(h,{...e})}):h(e)}},8453:(e,n,i)=>{i.d(n,{R:()=>d,x:()=>c});var s=i(6540);const l={},r=s.createContext(l);function d(e){const n=s.useContext(r);return s.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function c(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(l):e.components||l:d(e.components),s.createElement(r.Provider,{value:n},e.children)}}}]);