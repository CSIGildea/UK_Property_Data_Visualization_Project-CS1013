### CS1013-Team 10 ###
Group project for Trinity College Dublin's CS1013 module. 

Our assignment was to build a program that displays data from the UK Land Registry pertaining to the sale and lease of properties in England and Wales between the years 1995 and part of 2017, using the processing library.

<img src="https://github.com/CSIGildea/UK-Property-Data-Visualization-Project---CS1013/blob/master/Map%20Screenshot.PNG?raw=true">

The processing library did not provide any form of premade widgets (such as buttons), so the entire GUI library had to be coded. Our program incorporated a handburger menu, for all interactions with the user. We also had different sizes of datasets to use, we tackled the largest dataset, which resulted in a 3.7gb SQL Table (of 22 million property entries). 


After the GUI and database portion was completed we then made use of the unfolding maps library to be able to plot the locations, we incorporated data visualization into the map by grouping data into clusters which changed size based on the data being viewed. Users could also search a postcode through the search bar, the program would then display the location of this postcode and all the previous records for properties sold or leased in this postcode. 


Our program was also able to present the user with graphical visualizations of the data, this included, trend graphs, bar charts, pie charts..etc All these forms of graphs were built from scratch using the processing library.

### Prerequisites

```
The program relies on two SQL tables which can be downloaded and loaded on a local SQL server.
Most of the functions of the program can be run using only the small table but the search function requires the larger table.
The small table is included in the repo. The larger table(3.7gb) is available on request.
```

## Built With

* Processing.org
* [unfoldingmaps](https://github.com/tillnagel/unfolding)
* MySQL

## Authors

* **[Seán Hassett](https://github.com/Sean-Hassett)**
* **[Conor Gildea](https://github.com/CSIGildea/)**
* **Tobias Hallen**
* **[Catalina Rete](https://github.com/catakitty)**
