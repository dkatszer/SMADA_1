rm(list = ls())
?warpbreaks
barplot(breaks ~ wool*tension, data = warpbreaks)
data <- tapply(warpbreaks$breaks,
               list(warpbreaks$wool,
                    warpbreaks$tension),
               mean)
barplot(data,
        beside = TRUE,
        col = c(‘‘steelblue3’’, ‘‘thistle3’’),
        bor = NA,
        main = ‘‘Mean Number of Warp Breaks\nby Tension and Wool’’,
        xlab = ‘‘Tension’’,
        ylab = ‘‘Mean Number of Breaks’’)
legend(locator(1),
       rownames(data),
       fill = c(‘‘steelblue3’’, ‘‘thistle3’’))

?iris
data(iris)
iris[1:5, ]

require(car)

sp(Sepal.Width ~ Sepal.Length | Species,
   data = iris,
   xlab = ‘‘Sepal Width’’,
   ylab = ‘‘Sepal Length’’,
   main = ‘‘Iris Data’’,
   labels = row.names(iris))

pairs(iris[1:4])

require(‘‘RColorBrewer’’)
display.brewer.pal(3, ‘‘Pastel1’’)

panel.hist <- function(x, ...)
{
  usr <- par("usr"); on.exit(par(usr))
  par(usr = c(usr[1:2], 0, 1.5) )
  h <- hist(x, plot = FALSE)
  breaks <- h$breaks; nB <- length(breaks)
  y <- h$counts; y <- y/max(y)
  rect(breaks[-nB], 0, breaks[-1], y, ...)
  # Removed "col = "cyan" from code block; original below
  # rect(breaks[-nB], 0, breaks[-1], y, col = "cyan", ...)
}

pairs(iris[1:4],
      panel = panel.smooth, # Optional smoother
      main = "Scatterplot Matrix for Iris Data Using pairs Function",
      diag.panel = panel.hist,
      pch = 16,
      col = brewer.pal(3, "Pastel1")[unclass(iris$Species)])

library(car)
scatterplotMatrix(~Petal.Length + Petal.Width + Sepal.Length +
                    Sepal.Width | Species,
                  data = iris,
                  col = brewer.pal(3, "Dark2"),
                  main="Scatterplot Matrix for Iris Data Using
                  \"car\" Package")

palette("default") # Return to default
detach("package:RColorBrewer", unload = TRUE)
detach("package:car", unload=TRUE)
rm(list = ls())

data(iris)

install.packages(‘‘scatterplot3d’’)
require(‘‘scatterplot3d’’)
scatterplot3d(iris[1:3])

s3d <- scatterplotd(iris[1:3],
                    pch = 16,
                    highlight.3d = TRUE,
                    type = ‘‘h’’,
                    main = ‘‘3D Scatter plot’’)

plane <- lm(iris$Petal.Length ~ iris$Sepal.Length + iris$Sepal.Width)
s3d$plane3d(plane)


install.packages(‘‘rgl’’)
require(‘‘rgl’’)
require(‘‘RColorBrewer’’)
plot3d(iris$Petal.Length, # x variable
       iris$Petal.Width, # y variable
       iris$Sepal.Length, # z variable
       xlab = "Petal.Length",
       ylab = "Petal.Width",
       zlab = "Sepal.Length",
       col = brewer.pal(3, "Dark2")[unclass(iris$Species)],
       size = 8)

detach("package:scatterplot3d", unload = TRUE)
detach("package:rgl", unload = TRUE)
detach("package:RColorBrewer", unload = TRUE)
rm(list = ls())
