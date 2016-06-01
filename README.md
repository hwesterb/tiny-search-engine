# tiny-search-engine

Converts prefix to infix and performs query.

example:
```
| + this is - so cool orderby occ asc
```

outputs:
```
((this + is) | (so - cool))
got 493 results in 0m 0s 61ms 342µs 683ns
```



Supports:

* Indexing documents based on their content.
* Find and list documents which contain a single provided key word.
* Order search results by different properties (e.g. relevance, popularity).
* Query language to support more involved searches.



Developed in 2014 for a school project.
