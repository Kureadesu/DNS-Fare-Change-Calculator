using System;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace DNS_Fare_Change_Calculator
{
    public partial class BulakanBalagtasWindow : Window
    {
        private const int CATEGORY_PROMPT = 0;
        private const int FARE_TIER1_REGULAR = 13;
        private const int FARE_TIER2_REGULAR = 15;
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

        public BulakanBalagtasWindow()
        {
            InitializeComponent();
            InitializeLocations();
            SetDefaultSelections();
        }

        private void InitializeLocations()
        {
            // Based on the Android arrays for Balagtas route
            locations = new List<LocationItem>
            {
                new LocationItem { Name = "Select Location", Category = CATEGORY_PROMPT },
                new LocationItem { Name = "Bagumbayan | San Jose", Category = 1 },
                new LocationItem { Name = "Matungao", Category = 1 },
                new LocationItem { Name = "Panginay Guiguinto", Category = 1 },
                new LocationItem { Name = "Panginay Balagtas", Category = 1 },
                new LocationItem { Name = "Wawa", Category = 2 }
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

            // Determine base fare per passenger
            int baseRegularFarePerPassenger = FARE_TIER1_REGULAR; // Default 13

            string locationBagumbayanSanJose = "Bagumbayan | San Jose";
            string locationWawa = "Wawa";

            bool isPickupBSJ = pickup.Name == locationBagumbayanSanJose;
            bool isDestWawa = destination.Name == locationWawa;
            bool isPickupWawa = pickup.Name == locationWawa;
            bool isDestBSJ = destination.Name == locationBagumbayanSanJose;

            // Rule: Bagumbayan/San Jose <-> Wawa = 15
            if ((isPickupBSJ && isDestWawa) || (isPickupWawa && isDestBSJ))
            {
                baseRegularFarePerPassenger = FARE_TIER2_REGULAR; // 15
            }

            // Same location check (Fare 0)
            if (pickup.Name == destination.Name)
            {
                baseRegularFarePerPassenger = 0;
            }

            // Apply discount
            int actualFarePerPassenger = baseRegularFarePerPassenger;
            if (isDiscounted && baseRegularFarePerPassenger > 0)
            {
                actualFarePerPassenger -= DISCOUNT_AMOUNT;
                if (actualFarePerPassenger < 0)
                {
                    actualFarePerPassenger = 0;
                }
            }

            // Calculate total fare
            int totalFare = actualFarePerPassenger * selectedPassengerCount;

            // Calculate change
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