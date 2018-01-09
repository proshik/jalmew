package main

import (
	"github.com/proshik/jamew/example_full/printer"
	"github.com/urfave/cli"
)

func main() {
	app := cli.NewApp()
	app.Name = "appName"

	printer.ToConsole("Hello world!")
}