---
name: "Bug Report"
about: "Open a bug report to help us identify issues with Terra."
title: "[Bug] <Put your title here>"
labels: "Type: Bug, Status: Pending"
assignees: ""

---

<!--
##############################################################################
## WARNING!                                                                 ##
## IGNORING THE FOLLOWING TEMPLATE WILL RESULT IN YOUR ISSUE BEING CLOSED   ##
##############################################################################
-->

## Pre-Issue Checklist

<!--
  Please go through this checklist item by item and make sure you have successfully completed each of these steps.
    - You must be on the LATEST version of Terra to receive any support. There is no support for older versions of Terra.
    - Make sure that there are no already existing issues open with your problem. If you open a duplicate, it will be closed as such.
    - Make sure that it is actually Terra causing the issue, and not another plugin.
      You can do this by testing to see if you can recreate the issue without Terra installed.
    - Make sure that this is not an issue with a specific Terra *pack*, and instead applies to all of Terra.
    - Make sure that you attach a copy of the latest.log file, if there are any exceptions thrown in the console.
      Putting *just* the exception IS NOT ENOUGH. We need to be able to check that there wasn't anything else before that caused it.
    - Make sure that you have filled out all the required information and given descriptions of everything.
    
    You must put an x in all the boxes you have completed. (Like this: [x])
    
    To make sure that your issue is rendered properly, you may check the "Preview" tab (below the title) to see a rendered version of it before you submit it.
-->

- [ ] I have checked that I am on the latest version of Terra.
- [ ] I have searched the github issue tracker for similar issues, including closed ones.
- [ ] I have made sure that this is not a bug with another mod or plugin, and it is Terra that is causing the issue.
- [ ] I have checked that this is an issue with Terra and not an issue with the pack I am using.
  <!-- If this is an issue with the default Terra pack, please open an issue on the pack repo: https://github.com/PolyhedralDev/TerraDefaultConfig/issues/new -->
- [ ] I have attached a copy of the `latest.log` file (If applicable)
- [ ] I have filled out and provided all the appropriate information.

## Environment

<!-- You can fill out the different items by putting the correct value beside each cell. -->
| Name                         | Value |
|------------------------------|-------|
| Terra Version                | <!-- Put your Terra version here (remove the comment) --> 
| Server Software              | <!-- Put your server software here (remove the comment) (eg. Spigot, Fabric, Paper, Geyser, etc.) (If you are using the specialized Region generator, put that here instead.) -->
| Server Software Version      | <!-- Put the version of your server software here. (remove the comment) -->
| Any External Plugins or Mods | <!-- Put a list of all the plugins or mods you have installed here. (remove the comment) (Make sure to NOT include any new lines.) -->
| Terra Packs In Use           | <!-- Put a list of all the Terra packs you have installed here. (remove the comment) (Make sure to NOT include any new lines.) -->


## Issue Description
<!--
    Put a quick description of the issue here.
    Example: 'When generating terrain, something causes the chunks to not load properly', etc.
-->

### Steps to reproduce
<!--
    Describe what you were doing when this happened.
    Make sure to include ALL information. Including anything you were doing before that may have caused it.
-->
1. <!-- Put step #1 here. -->
2. <!-- Put step #2 here. -->
3. <!-- etc.              -->

### Expected behavior
<!-- Describe what you think *should* happen here: -->

### Actual behavior
<!-- Describe what *actually* happens here: -->
<!-- example: When I do _______, it actually does _______ -->

### The full stacktrace of the exception thrown

<!--
    If Terra logs an exception, please put it in the following section: (You will find any error logs in your console, or your latest.log)
    If the stack trace is over ~50 lines, then please just upload it instead (and don't put anything here.)
-->
```

```
Reasons why the log isn't uploaded:
<!-- Tick this box if Terra didn't log any exceptions. -->
- [ ] There was no stack trace or error log.
<!-- Tick this box if the exception was too long. -->
- [ ] The stack trace was over 50 lines, so I opted to only attach a file as to not clutter up the issue.