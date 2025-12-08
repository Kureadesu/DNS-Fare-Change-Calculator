using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace DNS_Fare_Change_Calculator
{
    public partial class BulakanGuiguintoWindow : Window
    {
        private const int REGULAR_FARE_AMOUNT = 13;
        private const int DISCOUNTED_FARE_DIFFERENCE = 2;
        private readonly SolidColorBrush DEFAULT_BUTTON_COLOR = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#FF814622"));
        private readonly SolidColorBrush SELECTED_BUTTON_COLOR = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#FF218C17"));

        private int selectedPassengerCount = 1;
        private int selectedAmountPaid = 20;

        public BulakanGuiguintoWindow()
        {
            InitializeComponent();
        }

        private void AmountButton_Click(object sender, RoutedEventArgs e)
        {
            Button clickedButton = sender as Button;
            int amount = int.Parse(clickedButton.Tag.ToString());
            selectedAmountPaid = amount;

            // Reset all amount buttons
            btnAmount20.Background = DEFAULT_BUTTON_COLOR;
            btnAmount50.Background = DEFAULT_BUTTON_COLOR;
            btnAmount100.Background = DEFAULT_BUTTON_COLOR;

            // Set selected button
            clickedButton.Background = SELECTED_BUTTON_COLOR;
        }

        private void PassengerButton_Click(object sender, RoutedEventArgs e)
        {
            Button clickedButton = sender as Button;
            int count = int.Parse(clickedButton.Tag.ToString());
            selectedPassengerCount = count;

            // Reset all passenger buttons
            btnPass1.Background = DEFAULT_BUTTON_COLOR;
            btnPass2.Background = DEFAULT_BUTTON_COLOR;
            btnPass3.Background = DEFAULT_BUTTON_COLOR;
            btnPass4.Background = DEFAULT_BUTTON_COLOR;
            btnPass5.Background = DEFAULT_BUTTON_COLOR;

            // Set selected button
            clickedButton.Background = SELECTED_BUTTON_COLOR;
        }

        private void DiscountedFare_Click(object sender, RoutedEventArgs e)
        {
            CalculateFare(true);
        }

        private void RegularFare_Click(object sender, RoutedEventArgs e)
        {
            CalculateFare(false);
        }

        private void CalculateFare(bool isDiscounted)
        {
            int change = ComputeFare(selectedAmountPaid, selectedPassengerCount, isDiscounted);

            if (change < 0)
            {
                displayChange.Text = "Short";
                MessageBox.Show("Amount paid is insufficient", "Payment Error", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else
            {
                displayChange.Text = change.ToString();
            }
        }

        private int ComputeFare(int paidAmount, int numPassengers, bool isDiscounted)
        {
            if (numPassengers <= 0) return 0;

            int totalFare = REGULAR_FARE_AMOUNT * numPassengers;

            if (isDiscounted)
            {
                totalFare = Math.Max(0, totalFare - (DISCOUNTED_FARE_DIFFERENCE * numPassengers));
            }

            return paidAmount - totalFare;
        }

        private void Clear_Click(object sender, RoutedEventArgs e)
        {
            // Reset to defaults
            selectedAmountPaid = 20;
            selectedPassengerCount = 1;
            displayChange.Text = "";

            // Reset button colors
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