plotDistribution <- function(file="D:/Dropbox/dottorato/Projects/KIT/workspace/TemporalFactAnnotator/home"){
  df <- read.csv(file="D:/Dropbox/dottorato/Projects/KIT/workspace/TemporalFactAnnotator/home",header=T)
  mean <- mean(df$occurrence)
  sd <- sd(df$occurrence)
  return (pnorm(df$occurrence,mean,sd))
}
