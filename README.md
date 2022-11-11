# Advanced Ban the comprehensive system with ban, mute and report with chatlog!

The plugin will run on BungeeCord and minimum java version 8. It also requires MySQl and a webserver (eg. apache2)
Make sure this plugin has been developed for English and German servers. The default configurations are in English.

**Functions:**
- ban, tempban, unban		| Bansystem
- mute, tempmute, unmute	| Mutesystem
- check						| Check player
- report					| Reportsystem
- webpanel					| Webpanel to manage reports and players
- chatlog					| Logs chat for easier reports

**Installation (plug, configure and play):**
1. unzip .zip file
2. put .jar file (plugin) into your BungeeCord plugins folder and run the server to generate config files
3. stop BungeeCord server and set your mysql data into the config
<template>
4. put abpanel folder into your webserver folder or wherever you want to run the panel
5. open config.php and set your mysql data. You can make more settings if you know a little more about the plugin
<template>
6. open messages.yml in your BungeeCord/plugins/Advanced_Ban_Bungee folder and change the link to your url from webserver
<template>
7. start BungeeCord server (restart webserver, not really needed)
-> feel free to ban other players!

**Permissions:**
- ab.tempmute		| Command
- ab.mute			| Command
- ab.unmute			| Command

- ab.tempban		| Command
- ab.ban			| Command
- ab.unban			| Command

- ab.cantbereported	| Action
- ab.check			| Command
- ab.panel			| Command

**Login security (Session key system):**
The player who is allowed to you the panel has to be on the server. Each time he joins the server, a session key is generated.
After typing /abpanel a link is generated with which he can call the panel. After the player left the server the session key will be deleted.