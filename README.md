# stacksearch

Little service, that performs a search by given parameters through API StackOverflow

## Usage

    lein run

## Examples

Пример запроса: 
>http://localhost:8080/search?tag=clojure&tag=scala

Пример ответа:
```
{
  "clojure" : {
    "total" : 7576,
    "answered" : 134
  }
  "scala" : {
    "total" : 5783,
    "answered" : 165
  }
}
```

## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
