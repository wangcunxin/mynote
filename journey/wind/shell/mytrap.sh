#!/bin/sh

continue=false
killByUser=$1
mytrap( )
{
        if [ $? -ne 0 ]; then
               if [ "$killByUser" == "true" ]; then
                echo '[print by workplatform::: killed by user,exit]'
            exit
        fi
               if [ $continue == 'false' ]; then
                   echo '[print by workplatform::: continue= false,exit]'
                   exit      
               else
                   echo '[print by workplatform::: continue=true,restore continue=false,continue..]'
           continue=false
               fi
              
        fi
}
trap 'mytrap $LINENO' DEBUG