using System;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace DNS_Fare_Change_Calculator
{
    public partial class BulakanMalolosWindow : Window
    {
        private const int CATEGORY_PROMPT = 0;
        private const int BASE_FARE = 13;
        private const int FARE_INCREMENT = 2;
        private const int BASE_DISTANCE_STEPS = 3;
        private const int DISCOUNT_AMOUNT = 2;

        private readonly SolidColorBrush DEFAULT_BUTTON_COLOR = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#FF814622"));
        private readonly SolidColorBrush SELECTED_BUTTON_COLOR = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#FF218C17"));

        private class LocationItem
        {
            public string Name { get; set; }
            public int Category { get; set; }

            public override string ToString()
            {
                return Name;
            }
        }

        private List<LocationItem> locations;
        private int selectedPassengerCount = 1;
        private int selectedAmountPaid = 20;

        public BulakanMalolosWindow()
        {
            InitializeComponent();
            InitializeLocations();
            SetDefaultSelections();
        }

        private void InitializeLocations()
        {
            // Based on the Android arrays for Malolos route
            // Categories 10-19 represent linear distance steps
            locations = new List<LocationItem>
            {
                new LocationItem { Name = "Select Location", Category = CATEGORY_PROMPT },
                new LocationItem { Name = "Bagumbayan | San Jose", Category = 10 },
                new LocationItem { Name = "Maysantol", Category = 11 },
                new LocationItem { Name = "San Nicolas", Category = 12 },
                new LocationItem { Name = "Pitpitan", Category = 13 },
                new LocationItem { Name = "Mambog", Category = 14 },
                new LocationItem { Name = "Matimbo", Category = 15 },
                new LocationItem { Name = "Panasahan", Category = 16 },
                new LocationItem { Name = "Bagna", Category = 17 },
                new LocationItem { Name = "Atlag", Category = 18 },
                new LocationItem { Name = "San Juan | Sto. Rosario", Category = 19 }
            };

            pickupComboBox.ItemsSource = new List<LocationItem>(locations);
            destinationComboBox.ItemsSource = new List<LocationItem>(locations);
            pickupComboBox.SelectedIndex = 0;
            destinationComboBox.SelectedIndex = 0;
        }

        private void SetDefaultSelections()
        {
            btnAmount20.Background = SELECTED_BUTTON_COLOR;
            btnPass1.Background = SELECTED_BUTTON_COLOR;
        }

        private void AmountButton_Click(object sender, RoutedEventArgs e)
        {
            Button clickedButton = sender as Button;
            int amount = int.Parse(clickedButton.Tag.ToString());
            selectedAmountPaid = amount;

            btnAmount20.Background = DEFAULT_BUTTON_COLOR;
            btnAmount50.Background = DEFAULT_BUTTON_COLOR;
            btnAmount100.Background = DEFAULT_BUTTON_COLOR;

            clickedButton.Background = SELECTED_BUTTON_COLOR;
        }

        private void PassengerButton_Click(object sender, RoutedEventArgs e)
        {
            Button clickedButton = sender as Button;
            int count = int.Parse(clickedButton.Tag.ToString());
            selectedPassengerCount = count;

            btnPass1.Background = DEFAULT_BUTTON_COLOR;
            btnPass2.Background = DEFAULT_BUTTON_COLOR;
            btnPass3.Background = DEFAULT_BUTTON_COLOR;
            btnPass4.Background = DEFAULT_BUTTON_COLOR;
            btnPass5.Background = DEFAULT_BUTTON_COLOR;

            clickedButton.Background = SELECTED_BUTTON_COLOR;
        }

        private void Location_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // No automatic calculation - wait for fare button click
        }

        private void DiscountedFare_Click(object sender, RoutedEventArgs e)
        {
            CalculateAndDisplayChange(true);
        }

        private void RegularFare_Click(object sender, RoutedEventArgs e)
        {
            CalculateAndDisplayChange(false);
        }

        private void CalculateAndDisplayChange(bool isDiscounted)
        {
            LocationItem pickup = pickupComboBox.SelectedItem as LocationItem;
            LocationItem destination = destinationComboBox.SelectedItem as LocationItem;

            if (pickup == null || destination == null ||
                pickup.Category == CATEGORY_PROMPT || destination.Category == CATEGORY_PROMPT)
            {
                MessageBox.Show("Please select locations.", "Selection Required", MessageBoxButton.OK, MessageBoxImage.Warning);
                displayChange.Text = "0";
                return;
            }

            // Calculate base fare per passenger
            int farePerPassenger = 0;

            if (pickup.Category == destination.Category)
            {
                farePerPassenger = 0; // Same location
            }
            else
            {
                // Calculate distance in steps
                int steps = Math.Abs(pickup.Category - destination.Category);

                // Base fare for first 3 steps (or less) is 13
                farePerPassenger = BASE_FARE;

                // Add 2 for each step beyond 3
                if (steps > BASE_DISTANCE_STEPS)
                {
                    int extraSteps = steps - BASE_DISTANCE_STEPS;
                    farePerPassenger += extraSteps * FARE_INCREMENT;
                }
            }

            // Apply discount
            if (isDiscounted && farePerPassenger > 0)
            {
                farePerPassenger -= DISCOUNT_AMOUNT;
                if (farePerPassenger < 0)
                {
                    farePerPassenger = 0;
                }
            }

            // Calculate total and change
            int totalFare = farePerPassenger * selectedPassengerCount;
            int change = selectedAmountPaid - totalFare;

            if (change < 0)
            {
                MessageBox.Show($"Amount paid is less than total fare (₱{totalFare})",
                    "Insufficient Payment", MessageBoxButton.OK, MessageBoxImage.Warning);
                displayChange.Text = "Short";
            }
            else
            {
                displayChange.Text = change.ToString();
            }
        }

        private void Clear_Click(object sender, RoutedEventArgs e)
        {
            selectedAmountPaid = 20;
            selectedPassengerCount = 1;
            displayChange.Text = "0";
            pickupComboBox.SelectedIndex = 0;
            destinationComboBox.SelectedIndex = 0;

            btnAmount20.Background = SELECTED_BUTTON_COLOR;
            btnAmount50.Background = DEFAULT_BUTTON_COLOR;
            btnAmount100.Background = DEFAULT_BUTTON_COLOR;

            btnPass1.Background = SELECTED_BUTTON_COLOR;
            btnPass2.Background = DEFAULT_BUTTON_COLOR;
            btnPass3.Background = DEFAULT_BUTTON_COLOR;
            btnPass4.Background = DEFAULT_BUTTON_COLOR;
            btnPass5.Background = DEFAULT_BUTTON_COLOR;
        }

        private void GoBack_Click(object sender, RoutedEventArgs e)
        {
            var mainWindow = new MainWindow();
            mainWindow.Show();
            this.Close();
        }
    }
}