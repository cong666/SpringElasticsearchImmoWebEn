# SpringElasticsearchImmoWebEn
Use ElasticSearch to optimize the house searching

## Introduction
### Technologies : 
     - SpringBoot2.1,Datatables, BootStrap.
     - MySQL, ElasticSearch.
     - Application Server : SpringBoot Embedded Server

![Search Page Show](src/main/resources/static/images/upload/github_readme1.png)
   
### Purpose of this application

Create the search engine using ElasticSearch for the real estate website.

### House index template

PUT :  http://localhost:9200/ccimmo/

{
  "settings": {
    "number_of_replicas": 3
  },
  "mappings": {
    "house": {
      "dynamic": false,
      "properties": {
        "houseId": {
          "type": "long"
        },
        "title": {
          "type": "text",
          "index": "analyzed"
        },
        "price": {
          "type": "integer"
        },
        "area": {
          "type": "integer"
        },
        "createTime": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "lastUpdateTime": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "cityEnName": {
          "type": "keyword"
        },
        "regionEnName": {
          "type": "keyword"
        },
        "direction": {
          "type": "integer"
        },
        "distanceToSubway": {
          "type": "integer"
        },
        "subwayLineName": {
          "type": "keyword"
        },
        "subwayStationName": {
          "type": "keyword"
        },
        "tags": {
          "type": "text"
        },
        "street": {
          "type": "keyword"
        },
        "district": {
          "type": "keyword"
        },
        "description": {
          "type": "text",
          "index": "analyzed"
        },
        "layoutDesc" : {
          "type": "text",
          "index": "analyzed"
        },
        "traffic": {
          "type": "text",
          "index": "analyzed"
        },
        "roundService": {
          "type": "text",
          "index": "analyzed"
        },
        "rentWay": {
          "type": "integer"
        },
        "suggest": {
          "type": "completion"
        },
        "location": {
          "type": "geo_point"
        }
      }
    }
  }
}

### TODO

  - The Frond-end need to be improved.
