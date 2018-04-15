#!/bin/bash
#set -x  # echo on


sqlite3 userinfo.db 'CREATE TABLE `users` (`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL, `age` INTEGER NOT NULL);'

